package com.management.service;

import com.management.common.ResponseResult;
import com.management.dto.user.UserDto;
import com.management.dto.user.UserLoginRequest;
import com.management.dto.user.UserRegisterRequest;

import java.util.Map;

/**
 * @author 夏明
 * @version 1.0
 */
public interface UserAuthService {

    /**
     * 用户登录接口, 返回Map中有键值对<token,tokenValue>
     *
     * @param user
     * @return ResponseResult<Map < String>>
     * @author 夏明
     * @date 2022/8/6 22:28
     */
    ResponseResult<Map<String, String>> login(UserLoginRequest user);

    /**
     * 用户退出登录
     *
     * @return ResponseResult
     * @author 夏明
     * @date 2022/8/6 23:31
     */
    ResponseResult<Void> logout();

    /**
     * 用户注册接口
     * @author 夏明
     * @date 2022/8/7 18:05
     * @param
     * @return ResponseResult
     */
    ResponseResult<Void> register(UserRegisterRequest user);

    /**
     * 获取当前用户信息
     * @author 夏明
     * @date 2022/8/8 10:16
     * @return ResponseResult<UserVo>
     */
    UserDto fetchCurrentUser();
}
