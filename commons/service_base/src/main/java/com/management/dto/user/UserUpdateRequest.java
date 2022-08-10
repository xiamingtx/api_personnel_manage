package com.management.dto.user;

import com.management.enums.Gender;
import lombok.Data;

/**
 * @author 夏明
 * @version 1.0
 */
@Data
public class UserUpdateRequest {

    private String nickname;

    private Gender gender;
}
