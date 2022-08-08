package com.management.service;

import com.management.common.ResponseResult;
import com.management.dto.user.UserRegisterRequest;
import com.management.dto.user.UserDto;
import com.management.dto.user.UserLoginRequest;

import java.util.Map;

/**
 * @author 夏明
 * @version 1.0
 */
public interface UserAuthService{

    ResponseResult<Map<String, String>> login(UserLoginRequest user);

    ResponseResult<Void> logout();

    ResponseResult<Void> register(UserRegisterRequest user);

    ResponseResult<Void> sendCode(String mail);

    UserDto fetchCurrentUser();
}
