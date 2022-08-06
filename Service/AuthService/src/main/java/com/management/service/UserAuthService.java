package com.management.service;

import com.management.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 夏明
 * @version 1.0
 */
@Service
public class UserAuthService implements UserDetailsService {

    @Resource
    UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.management.entity.User user = repository.findUserByUsername(username);
        if(user == null) throw new UsernameNotFoundException("用户 "+username+" 登录失败，用户名不存在！");
        return User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
}
