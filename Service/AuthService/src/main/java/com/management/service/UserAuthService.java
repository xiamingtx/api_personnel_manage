package com.management.service;

import com.management.common.ResponseResult;
import com.management.dto.UserCreateRequest;
import com.management.dto.UserLoginRequest;
import com.management.entity.User;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author 夏明
 * @version 1.0
 */
@Service
public interface UserAuthService{

    ResponseResult<Map<String, String>> login(UserLoginRequest user);

    ResponseResult logout();

    ResponseResult register(UserCreateRequest user);

    ResponseResult sendCode(String mail);
}
