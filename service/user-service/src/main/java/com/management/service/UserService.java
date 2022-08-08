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

    List<UserDto> list();

    UserDto get(String id);

    ResponseResult<Void> create(UserCreateRequest userCreateRequest);

    ResponseResult<Void> update(String id, UserUpdateRequest userUpdateRequest);

    ResponseResult<Void> delete(String id);
}
