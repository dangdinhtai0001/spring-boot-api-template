-- ---------------------------------------------- user table

INSERT INTO USER (USERNAME, LOCKED, ENABLED)
VALUES ('root', false, true),  ('admin', false, true);
-- ---------------------------------------------- role table

INSERT INTO ROLE (NAME)
VALUES ('ROOT');
INSERT INTO ROLE (NAME)
VALUES ('ADMIN');
INSERT INTO ROLE (NAME)
VALUES ('USER');
INSERT INTO ROLE (NAME)
VALUES ('GUEST');

-- ---------------------------------------------- permission table
-- permission name = resource_name + __ + privilege_name

INSERT INTO PERMISSION (NAME)
VALUES
('USER__CREATE'),
('USER__READ'),
('USER__UPDATE'),
('USER__DELETE'),
('ADMIN__CREATE'),
('ADMIN__READ'),
('ADMIN__UPDATE'),
('ADMIN__DELETE')
;

-- ---------------------------------------------- role_permission table

-- permission_id, role_id
--
-- select r.name role, group_concat(p.NAME) permission
-- from role r, permission p, role_permission rp
-- where r.ID = rp.ROLE_ID and p.ID = rp.PERMISSION_ID;

-- user: root (1)
-- user: admin (2)

insert into role_permission
values
(1,1),
(2,1),
(3,1),
(4,1),
(5,1),
(6,1),
(7,1),
(8,1),
(1,2),
(2,2),
(3,2),
(4,2);

-- ---------------------------------------------- user_role table

--user_id, role_id

insert into user_role
values
(1,1),
(2,2)
;

commit;

