package com.management.service.impl;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.management.common.ResponseResult;
import com.management.config.SecurityConfiguration;
import com.management.common.UserDetailsEntity;
import com.management.dto.UserCreateRequest;
import com.management.entity.User;
import com.management.service.UserAuthService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
    StringRedisTemplate template;

    /**
     * 用户登录接口
     *
     * @param user
     * @return ResponseResult<Map < String>>
     * @author 夏明
     * @date 2022/8/6 22:28
     */
    @Override
    public ResponseResult<Map<String, String>> login(UserCreateRequest user) {
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
     * 用户退出登录
     *
     * @return ResponseResult
     * @author 夏明
     * @date 2022/8/6 23:31
     */
    @Override
    public ResponseResult logout() {
        // 获取SecurityContextHolder中的username
        // 这里不需要删除SecurityContextHolder中的值 因为在其他请求进入过滤器时 我们会在redis中获取对象 如果为空就返回未登录
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsEntity userDetailsEntity = (UserDetailsEntity) authentication.getPrincipal();
        String username = userDetailsEntity.getUser().getUsername();
        // 删除redis中的值
        template.delete("login:" + username);
        return new ResponseResult<>(200, "注销成功");
    }
}
