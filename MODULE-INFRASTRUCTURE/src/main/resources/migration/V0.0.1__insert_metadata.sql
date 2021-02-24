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
VALUES ('USER__CREATE');

INSERT INTO PERMISSION (NAME)
VALUES ('USER__READ');

INSERT INTO PERMISSION (NAME)
VALUES ('USER__UPDATE');

INSERT INTO PERMISSION (NAME)
VALUES ('USER__DELETE');

commit;

