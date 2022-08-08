package com.management.service.impl;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.management.common.ResponseResult;
import com.management.common.UserDetailsEntity;
import com.management.config.SecurityConfiguration;
import com.management.dto.user.UserRegisterRequest;
import com.management.dto.user.UserDto;
import com.management.dto.user.UserLoginRequest;
import com.management.entity.User;
import com.management.exception.BizException;
import com.management.exception.ExceptionType;
import com.management.mapper.UserMapper;
import com.management.repository.UserRepository;
import com.management.service.UserAuthService;
import org.flywaydb.core.internal.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author 夏明
 * @version 1.0
 */
@Service
public class UserAuthServiceImpl implements UserAuthService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private StringRedisTemplate template;

    //JavaMailSender是专门用于发送邮件的对象，自动配置类已经提供了Bean
    @Resource
    private JavaMailSender sender;

    @Value("${spring.mail.username}")
    private String me;

    @Resource
    private UserMapper mapper;

    @Resource
    private UserRepository repository;

    @Resource
    UserServiceImpl userService;


    /**
     * 用户登录接口
     *
     * @param user
     * @return ResponseResult<Map < String>>
     * @author 夏明
     * @date 2022/8/6 22:28
     */
    @Override
    public ResponseResult<Map<String, String>> login(UserLoginRequest user) {
        // AuthenticationManager authenticate进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        // 如果认证没通过, 给出对应的提示
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("登陆失败");
        }
        // 如果认证通过了, 通过username生成jwt jwt存入ResponseResult返回
        UserDetailsEntity userDetailsEntity = (UserDetailsEntity) authenticate.getPrincipal();
        String username = userDetailsEntity.getUser().getUsername();
        String token = JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConfiguration.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConfiguration.SECRET.getBytes()));
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        // 把完整的用户信息存入redis, username作为key
        template.opsForValue().set("login:" + username, JSON.toJSONString(userDetailsEntity),
                SecurityConfiguration.EXPIRATION_TIME, TimeUnit.MILLISECONDS);

        return new ResponseResult<>(2000, "登录成功", map);
    }

    /**
     * 根据邮箱发送邮件
     * @author 夏明
     * @date 2022/8/7 18:10
     * @param mail
     * @return ResponseResult
     */
    @Override
    public ResponseResult<Void> sendCode(String mail) {
        // Todo: 使用消息队列重构
        // 构建redis键
        String redisKey = "register:" + mail;
        // 随机生成value
        String text = String.valueOf((int)((Math.random()*9+1)*100000));
        // 在redis中存放五分钟
        template.opsForValue().set(redisKey, text, 300, TimeUnit.SECONDS);
        // 发送value给邮箱
        //SimpleMailMessage是一个比较简易的邮件封装，支持设置一些比较简单内容
        SimpleMailMessage message = new SimpleMailMessage();
        //设置邮件标题
//        message.setSubject("【华北电力大学教务处】关于近期学校对您的处分决定");
        message.setSubject("【人力资源管理系统】注册码");
        //设置邮件内容
//        message.setText("XXX同学您好，经监控和教务巡查发现，您近期存在旷课、迟到、早退、上课刷抖音行为，" +
//                "现已通知相关辅导员，请手写5000字书面检讨，并在2022年4月1日17点前交到辅导员办公室。");
        message.setText("验证码: " + text);
        //设置邮件发送给谁，可以多个，这里就发给你的QQ邮箱
        message.setTo(mail);
        //邮件发送者，这里要与配置文件中的保持一致
        message.setFrom(me);
        //OK，万事俱备只欠发送
        sender.send(message);
        return new ResponseResult<Void>(2000, "发送完成");
    }

    /**
     * 用户注册接口
     * @author 夏明
     * @date 2022/8/7 18:05
     * @param
     * @return ResponseResult
     */
    @Override
    @Transactional
    public ResponseResult<Void> register(UserRegisterRequest userRegisterRequest) {
        // 查询用户是否已存在
        String username = userRegisterRequest.getUsername();
        if (repository.findUserByUsername(username) != null) {
            // 抛出用户已存在异常
            throw new BizException(ExceptionType.USER_NAME_DUPLICATE);
        }
        // 构建redis键
        String redisKey = "register:" + userRegisterRequest.getMail();
        // 查看redis中是否已经存了验证码
        String verifyCode = template.opsForValue().get(redisKey);
        if (StringUtils.hasText(verifyCode)) {
            boolean ok = verifyCode.equals(userRegisterRequest.getCode());
            // 验证码错误
            if (!ok) {
                return new ResponseResult<>(2001, "验证码错误");
            }
            // 利用mapper进行转换
            User user = mapper.createEntity(userRegisterRequest);
            userService.addUser(user);

            // redis中删除缓存
            template.delete(redisKey);
            return new ResponseResult<>(2000, "注册成功!");
        }
        return new ResponseResult<>(2001, "注册失败!");
    }

    /**
     * 用户退出登录
     *
     * @return ResponseResult
     * @author 夏明
     * @date 2022/8/6 23:31
     */
    @Override
    public ResponseResult<Void> logout() {
        // 获取SecurityContextHolder中的username
        // 这里不需要删除SecurityContextHolder中的值 因为在其他请求进入过滤器时 我们会在redis中获取对象 如果为空就返回未登录
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsEntity userDetailsEntity = (UserDetailsEntity) authentication.getPrincipal();
        String username = userDetailsEntity.getUser().getUsername();
        // 删除redis中的值
        template.delete("login:" + username);
        return new ResponseResult<>(2000, "注销成功");
    }

    /**
     * 获取当前用户信息
     * @author 夏明
     * @date 2022/8/8 10:16
     * @return ResponseResult<UserVo>
     */
    @Override
    public UserDto fetchCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = repository.findUserByUsername(authentication.getName());
        return mapper.toDto(user);
    }
}
