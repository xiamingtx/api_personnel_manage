package com.management.mapper;


import com.management.dto.role.RoleDto;
import com.management.entity.Role;
import com.management.vo.RoleVo;
import org.mapstruct.Mapper;

/**
 * @author 夏明
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleDto toDto(Role role);

    RoleVo toVo(RoleDto roleDto);
}
