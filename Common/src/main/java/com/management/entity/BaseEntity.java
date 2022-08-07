package com.management.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass // 基类
@Data
public abstract class BaseEntity {

    @Id
    @GeneratedValue(generator = "ksuid")
    @GenericGenerator(name = "ksuid", strategy = "com.management.utils.KsuidIdentifierGenerator")
    private String id; // 要添加MappedSuperclass否则子类不会检测id

    private Integer isDeleted = 0; // 已删除 默认为0

    @CreationTimestamp // 创建时更新时间
    private Date createdTime;

    @UpdateTimestamp // 修改时更新时间
    private Date updatedTime;
}