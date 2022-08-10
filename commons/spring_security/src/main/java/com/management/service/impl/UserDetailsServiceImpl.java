package com.management.service.impl;

import com.management.entity.Role;
import com.management.entity.User;
import com.management.entity.UserDetailsEntity;
import com.management.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 夏明
 * @version 1.0
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findUserByUsername(username);
        // 如果查询不到数据就抛出异常
        if(Objects.isNull(user)) {
            throw new RuntimeException("用户 "+username+" 登录失败，用户名不存在！");
        }
        // 查询对应的权限信息
        List<String> list = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
        // 封装成UserDetails对象返回(LoginUser实现了)
        return new UserDetailsEntity(user, list);
    }

}
