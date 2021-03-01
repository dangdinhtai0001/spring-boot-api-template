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
       ('GRANT__PRIVILEGE'),
       ('RESET__PASSWORD')
;

-- ---------------------------------------------- role_permission table

-- permission_id, role_id

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

#  ---------------------------------------------- user_role table

# user_id, role_id

insert into user_role(user_id, role_id)
values (1, 1),
       (2, 2)
;

commit;

