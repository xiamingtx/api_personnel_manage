ALTER TABLE `personnel_management`.`role`
       MODIFY COLUMN `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
       COMMENT '创建时间' AFTER `is_deleted`,
       MODIFY COLUMN `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' AFTER `created_time`;