package com.management.service.impl;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.management.common.ResponseResult;
import com.management.config.SecurityConfiguration;
import com.management.dto.user.UserDto;
import com.management.dto.user.UserLoginRequest;
import com.management.dto.user.UserRegisterRequest;
import com.management.entity.Role;
import com.management.entity.User;
import com.management.entity.UserDetailsEntity;
import com.management.exception.BizException;
import com.management.exception.ExceptionType;
import com.management.mapper.UserMapper;
import com.management.repository.UserRepository;
import com.management.service.UserAuthService;
import com.management.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author 夏明
 * @version 1.0
 */
@Service
@Slf4j
public class UserAuthServiceImpl implements UserAuthService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private StringRedisTemplate template;

    @Resource
    private UserMapper mapper;

    @Resource
    private UserRepository repository;

    @Resource
    PasswordEncoder passwordEncoder;


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
        Map<String, String> map = getAndStoreToken(userDetailsEntity, username);
        return new ResponseResult<>(2000, "登录成功", map);
    }

    private Map<String, String> getAndStoreToken(UserDetailsEntity userDetailsEntity, String username) {
        // 这里是创建access_token
        String accessToken = JWTUtils.createJWT(username, SecurityConfiguration.EXPIRATION_TIME);
        // 这里是创建refresh_token
        String refreshToken = JWTUtils.createJWT(username, SecurityConfiguration.EXPIRATION_TIME * 2);
        Map<String, String> map = new HashMap<>();
        map.put("access_token", accessToken);
        map.put("refresh_token", refreshToken);
        // 把完整的用户信息存入redis, username作为key
        template.opsForValue().set("access_token:" + username, JSON.toJSONString(userDetailsEntity),
                SecurityConfiguration.EXPIRATION_TIME, TimeUnit.MILLISECONDS);
        template.opsForValue().set("refresh_token:" + username, JSON.toJSONString(userDetailsEntity),
                SecurityConfiguration.EXPIRATION_TIME * 2, TimeUnit.MILLISECONDS);
        return map;
    }


    @Override
    @Transactional
    @Modifying
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
            addUser(user);

            // redis中删除缓存
            template.delete(redisKey);
            return new ResponseResult<>(2000, "注册成功!");
        }
        return new ResponseResult<>(2001, "注册失败!");
    }

    public void addUser(User user) {
        // 对密码进行加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 默认昵称
        user.setNickname(user.getUsername());
        // 设置默认权限
        Role role = new Role();
        role.setId("1");
        user.setRoles(Collections.singletonList(role));
        repository.save(user);
    }

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

    @Override
    public UserDto fetchCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = repository.findUserByUsername(authentication.getName());
        return mapper.toDto(user);
    }

    @Override
    public ResponseResult<Map<String, String>> refresh(String refreshToken) {
//        log.info("收到: {}", refreshToken);
        String username = JWTUtils.getSubject(refreshToken);
        String redisKey = "refresh_token:" + username;
        Long expire = template.getExpire(redisKey);
        if (expire != null && expire > 0) {
            UserDetailsEntity userDetailsEntity = JSON.parseObject(template.opsForValue().get(redisKey), UserDetailsEntity.class);
            Map<String, String> map = getAndStoreToken(userDetailsEntity, username);
            return new ResponseResult<>(2000, "刷新成功",map);
        }
        return new ResponseResult<>(2001, "申请刷新失败");
    }
}
