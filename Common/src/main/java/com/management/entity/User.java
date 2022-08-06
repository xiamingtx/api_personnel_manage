package com.management.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 夏明
 * @version 1.0
 */
@Data
@Entity
@Table(name = "users")    //对应的数据库中表名称
public class User {

    @GeneratedValue(strategy = GenerationType.IDENTITY)   //生成策略，这里配置为自增
    @Column(name = "id")    //对应表中id这一列
    @Id
    int uid;

    @Column(name = "username")   //对应表中username这一列
    String username;

    @Column(name = "password")   //对应表中password这一列
    String password;

    @Column(name = "role")
    String Role;
}
