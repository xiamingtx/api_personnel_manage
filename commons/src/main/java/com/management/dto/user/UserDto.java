package com.management.dto.user;

import com.management.dto.role.RoleDto;
import com.management.enums.Gender;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author 夏明
 * @version 1.0
 */
@Data
public class UserDto {

    private String id;

    private String username;

    private String nickname;

    private List<RoleDto> roles; // 关联数据

    private Gender gender;

    private Date lastLoginTime;
}
