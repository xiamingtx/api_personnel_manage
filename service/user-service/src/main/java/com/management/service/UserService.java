package com.management.service;

import com.management.common.ResponseResult;
import com.management.dto.user.UserCreateRequest;
import com.management.dto.user.UserDto;
import com.management.dto.user.UserUpdateRequest;

import java.util.List;

/**
 * @author 夏明
 * @version 1.0
 */
public interface UserService {

    /**
     * 查询用户列表
     * @author 夏明
     * @date 2022/8/8 14:28
     * @return List<UserDto>
     */
    List<UserDto> list();

    /**
     * 根据id查询用户
     * @author 夏明
     * @date 2022/8/8 14:28
     * @param id
     * @return UserDto
     */
    UserDto get(String id);

    /**
     * 添加用户
     * @author 夏明
     * @date 2022/8/8 14:43
     * @param userCreateRequest
     */
    ResponseResult<Void> create(UserCreateRequest userCreateRequest);

    /**
     * 修改用户
     * @author 夏明
     * @date 2022/8/8 18:16
     * @param id
     * @param userUpdateRequest
     * @return ResponseResult<Void>
     */
    ResponseResult<Void> update(String id, UserUpdateRequest userUpdateRequest);

    /**
     * 删除用户(逻辑删除)
     * @author 夏明
     * @date 2022/8/8 19:59
     * @param id
     * @return ResponseResult<Void>
     */
    ResponseResult<Void> delete(String id);
}
