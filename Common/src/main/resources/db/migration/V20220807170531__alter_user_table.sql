ALTER TABLE `personnel_management`.`user`
    MODIFY COLUMN `last_login_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP COMMENT '最后登录IP' AFTER `last_login_ip`,
    MODIFY COLUMN `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
        COMMENT '创建时间' AFTER `is_deleted`,
    MODIFY COLUMN `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' AFTER `created_time`;