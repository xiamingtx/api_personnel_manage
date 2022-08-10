ALTER TABLE `personnel_management`.`user`
    MODIFY COLUMN `last_login_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后登录IP' AFTER `last_login_ip`;