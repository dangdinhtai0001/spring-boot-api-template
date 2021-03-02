-- ---------------------------------------------- user table

INSERT INTO USER (USERNAME, LOCKED, ENABLED)
VALUES ('root', false, true),
       ('admin', false, true);

# 1	AUTO_SYSTEM	2021-03-01 21:51:52	AUTO_SYSTEM	2021-03-01 21:51:52		1	0		root
# 2	AUTO_SYSTEM	2021-03-01 21:51:52	AUTO_SYSTEM	2021-03-01 21:51:52		1	0		admin
-- ---------------------------------------------- role table

INSERT INTO ROLE (NAME)
VALUES ('ROOT'),
       ('ADMIN'),
       ('USER'),
       ('GUEST');

-- ---------------------------------------------- permission table
-- permission name = resource_name + __ + privilege_name

INSERT INTO PERMISSION (NAME)
VALUES ('USER__CREATE'),
       ('USER__READ'),
       ('USER__UPDATE'),
       ('USER__DELETE'),
       ('ADMIN__CREATE'),
       ('ADMIN__READ'),
       ('ADMIN__UPDATE'),
       ('ADMIN__DELETE'),
       ('PRIVILEGE__GRANT'),
       ('PASSWORD__RESET')
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
       (1, 2),
       (2, 2),
       (3, 2),
       (4, 2);

# -------------------------------------------- user_role table

insert into user_role(user_id, role_id)
values (1, 1),
       (2, 2)
;

# -------------------------------------------- create view: VIEW_USER_PERMISSIONS

drop view if exists VIEW_USER_PERMISSIONS;

CREATE VIEW VIEW_USER_PERMISSIONS as
select
    u.id user_id, r.name role, p.permissions
from
    user u join user_role ur on u.id = ur.USER_ID join role r on r.ID = ur.ROLE_ID,
    (select r.name role, group_concat(p.name) permissions from
        role r join role_permission rp on r.id = rp.ROLE_ID
               join permission p on p.id = rp.PERMISSION_ID
     group by r.name) p
where
        r.name = p.role
;


commit;

