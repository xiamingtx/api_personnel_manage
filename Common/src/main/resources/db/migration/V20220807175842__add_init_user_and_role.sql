INSERT INTO `user` (id, username, nickname, password, gender, mail, mobile)
VALUES ('1', 'xm', 'xm', '$2a$10$8LRsDrZyptmX3ePzbPbmvOU9DIqyIk3o4WR4Qj0LgjTknQwoXtOjS', 'MALE',
        '18307478153@163.com', '18307478153');
INSERT INTO `role` (id, name, title)
VALUES ('1', 'user', '普通用户');
INSERT INTO `role` (id, name, title)
VALUES ('2', 'admin', '管理员');
INSERT INTO `user_role` (user_id, role_id)
VALUES ('1', '1');
INSERT INTO `user_role` (user_id, role_id)
VALUES ('1', '2');