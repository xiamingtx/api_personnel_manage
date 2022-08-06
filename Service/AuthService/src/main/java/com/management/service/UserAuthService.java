package com.management.service;

import com.management.ResponseResult;
import com.management.dto.LoginUser;
import com.management.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author 夏明
 * @version 1.0
 */
@Service
public interface UserAuthService{

    ResponseResult<Map<String, String>> login(User user);

    ResponseResult logout();
}
