package com.management.mapper;

import com.management.dto.user.UserCreateRequest;
import com.management.dto.user.UserRegisterRequest;
import com.management.dto.user.UserDto;
import com.management.entity.User;
import com.management.vo.UserVo;
import org.mapstruct.Mapper;

/**
 * @author 夏明
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    User createEntity(UserRegisterRequest userRegisterRequest);

    User createEntity(UserCreateRequest userCreateRequest);

    UserDto toDto(User user);

    UserVo toVo(UserDto userDto);
}
