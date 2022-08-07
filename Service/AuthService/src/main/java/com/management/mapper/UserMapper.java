package com.management.mapper;

import com.management.dto.UserCreateRequest;
import com.management.entity.User;
import org.mapstruct.Mapper;

/**
 * @author 夏明
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    User createEntity(UserCreateRequest userCreateRequest);
}
