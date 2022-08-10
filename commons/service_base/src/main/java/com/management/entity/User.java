package com.management.entity;

import com.management.enums.Gender;
import lombok.Data;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @author 夏明
 * @version 1.0
 */
@Data
@Entity
public class User extends BaseEntity {

    @Column(name = "username", unique = true)   //对应表中username这一列
    String username;

    private String nickname;

    @Column(name = "password")   //对应表中password这一列
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles; // 关联数据

    private String mail;

    private String mobile;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Boolean locked = false;

    private Boolean enabled = true;

    private String lastLoginIp;

    private Date lastLoginTime;
}
