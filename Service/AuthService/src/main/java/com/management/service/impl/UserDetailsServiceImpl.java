package com.management.service.impl;

import com.management.dto.LoginUser;
import com.management.entity.User;
import com.management.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

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
        if(Objects.isNull(user)) throw new UsernameNotFoundException("用户 "+username+" 登录失败，用户名不存在！");
        // 封装成UserDetails对象返回(LoginUser实现了)
        return new LoginUser(user);
    }

}
