ALTER TABLE `personnel_management`.`user`
    MODIFY COLUMN `gender` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '性别' AFTER `password`,
    ADD COLUMN `mail` varchar(128) NOT NULL COMMENT '邮箱' AFTER `gender`,
    ADD COLUMN `mobile` varchar(32) NULL COMMENT '联系方式' AFTER `mail`;