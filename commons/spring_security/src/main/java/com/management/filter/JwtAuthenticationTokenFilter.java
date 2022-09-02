package com.management.filter;

import com.alibaba.fastjson.JSON;
import com.management.entity.UserDetailsEntity;
import com.management.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author 夏明
 * @version 1.0
 */
@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    StringRedisTemplate template;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取token 前端放在请求头中,同时要保证键要和后端一致
        // 将token是否应该刷新交给前端来判断,后端
        String accessToken = request.getHeader("access_token");
//        String refreshToken = request.getHeader("refresh_token");
        if (!StringUtils.hasText(accessToken)) {
            // 放行
            filterChain.doFilter(request, response);
            return;
        }
        // 解析token
        // 如果refreshToken都解析过期,在verify会报错,会直接返回:重新登陆
        String username = JWTUtils.getSubject(accessToken);

        if (username == null) {
            throw new RuntimeException("token非法");
        }

        // 如果refreshToken没过期,我们获取一下accessToken的剩余时间
        // 从redis中获取access_token
        String accessKey = "access_token:" + username;
        UserDetailsEntity userDetailsEntity = JSON.parseObject(template.opsForValue().get(accessKey), UserDetailsEntity.class);

        if (Objects.isNull(userDetailsEntity)) {
            throw new RuntimeException("用户未登录");
        }
        // 存入SecurityContextHolder
        // SecurityContextHolder是SpringSecurity最基本的组件了，是用来存放SecurityContext的对象,
        // 默认是使用ThreadLocal实现,这样就保证了本线程内所有的方法都可以获得SecurityContext对象。
        //  2) SecurityContextHolder还有其他两种模式，分别为SecurityContextHolder.MODE_GLOBAL
        //  和SecurityContextHolder.MODE_INHERITABLETHREADLOCAL，前者表示SecurityContextHolder对象是全局的,
        //  应用中所有线程都可以访问；后者用于线程有父子关系的情境中，线程希望自己的子线程和自己有相同的安全性。
        //  3)大部分情况下我们不需要修改默认的配置，ThreadLocal是最常用也是最合适大部分应用的。
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetailsEntity, null, userDetailsEntity.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // 放行
        filterChain.doFilter(request, response);
    }
}
