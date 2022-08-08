ALTER TABLE `personnel_management`.`user`
    MODIFY COLUMN `mail` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '邮箱' AFTER `gender`;