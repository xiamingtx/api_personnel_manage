package com.management.vo;

import com.management.enums.Gender;
import lombok.Data;

import java.util.List;

/**
 * @author 夏明
 * @version 1.0
 */
@Data
public class UserVo {
    private String username;

    private String nickname;

    private Gender gender;

    private List<RoleVo> roles; // 关联数据
}
