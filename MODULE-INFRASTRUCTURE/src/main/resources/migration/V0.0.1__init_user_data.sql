-- ---------------------------------------------- user table

INSERT INTO USER (USERNAME, LOCKED, ENABLED)
VALUES ('root', false, true),
       ('admin', false, true),
       ('user', false, true)
;

# 1	AUTO_SYSTEM	2021-03-01 21:51:52	AUTO_SYSTEM	2021-03-01 21:51:52		1	0		root
# 2	AUTO_SYSTEM	2021-03-01 21:51:52	AUTO_SYSTEM	2021-03-01 21:51:52		1	0		admin
-- ---------------------------------------------- role table

INSERT INTO ROLE (NAME, DESCRIPTION, LEVEL)
VALUES ('ROOT', 'Tài khoản có quyền cao nhất', 0),
       ('ADMIN', 'Tài khoản quản trị', 1),
       ('USER', 'Tài khoản người dung mặc định', 2),
       ('GUEST', 'Tài khoản khách', 3);

-- ---------------------------------------------- permission table
-- permission name = resource_name + __ + privilege_name

INSERT INTO PERMISSION (NAME, DESCRIPTION)
VALUES ('USER__CREATE', 'Tạo tài khoản bình thường'),
       ('USER__READ', 'Xem thông tin tài khoản khác'),
       ('USER__UPDATE', 'Cập nhật thông tin tài khoản bình thường khác'),
       ('USER__DELETE', 'Xóa tài khoản bình thường khác'),
       ('ADMIN__CREATE', 'Tạo tài khoản quản trị'),
       ('ADMIN__READ', 'Xem thông tin tài khoản quản trị'),
       ('ADMIN__UPDATE', 'Cập nhật thông tin cho tài khoản quản trị'),
       ('ADMIN__DELETE', 'Xóa tài khoản quản trị'),
       ('GRANT_PRIVILEGE', 'Cấp phát quyền cho các tài khoản có level thấp hơn'),
       ('RESET_OTHER_PASSWORD', 'Reset lại mật khẩu cho các tài khoản có level thấp hơn'),
       ('PROFILE__READ', 'Xem thông tin tài khoản'),
       ('PROFILE__UPDATE', 'Cập nhật thông tin profile')
;

-- ---------------------------------------------- role_permission table

insert into role_permission(permission_id, role_id)
values (1, 1),
       (2, 1),
       (3, 1),
       (4, 1),
       (5, 1),
       (6, 1),
       (7, 1),
       (8, 1),
       (9, 1),
       (10, 1),
       (11, 1),
       (12, 1),
       (1, 2),
       (2, 2),
       (3, 2),
       (4, 2),
       (9, 2),
       (10, 2),
       (11, 2),
       (12, 2),
       (2, 3),
       (11, 3),
       (12, 3),
       (2, 4)
;

# -------------------------------------------- user_role table

insert into user_role(user_id, role_id)
values (1, 1),
       (2, 2),
       (3, 3),
       (3, 4)
;

# -------------------------------------------- create view: VIEW_USER_PERMISSIONS

drop view if exists VIEW_USER_PERMISSIONS;

CREATE VIEW VIEW_USER_PERMISSIONS as
select u.id                                 user_id,
       group_concat(p.role)                 roles,
       group_concat(DISTINCT p.permissions) permissions
from (select r.name role, group_concat(p.name) permissions
      from role r
               join role_permission rp on r.id = rp.ROLE_ID
               join permission p on p.id = rp.PERMISSION_ID
      group by r.name) p
         join role r on r.name = p.role
         join user_role ur on r.ID = ur.ROLE_ID
         join user u on u.id = ur.USER_ID
group by u.id
;

# -------------------------------------------- create view: VIEW_ROLE_PERMISSIONS

drop view if exists VIEW_ROLE_PERMISSIONS;

CREATE VIEW VIEW_ROLE_PERMISSIONS as
select r.name role, group_concat(p.name) permissions
from role r
         join role_permission rp on r.id = rp.ROLE_ID
         join permission p on p.id = rp.PERMISSION_ID
group by r.name;

# -------------------------------------------- API_URL table

insert into API_URL (URL, NAME, DESCRIPTION)
values ('/auth/create-account', 'CREATE_ACCOUNT', ''),
       ('/auth/sign-in/password', 'SIGN_IN_BY_PASSWORD', ''),
       ('/auth/create-qr-code', 'CREATE_QR_CODE', '')
;

# -------------------------------------------- API_URL_PERMISSION

insert into API_URL_PERMISSION(URL_ID, PERMISSION_ID)
values (1, 1),
       (3, 11)
;
commit;

