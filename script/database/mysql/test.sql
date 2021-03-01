select * from user;
select * from user_role;
select * from role;
select * from role_permission;
select * from permission;

select u.id, u.USERNAME, r.name, p.name, GROUP_CONCAT( if(p.name = '', null, p.name) SEPARATOR ',') from
    user u join user_role ur on u.id = ur.USER_ID
           join role r on r.id = ur.ROLE_ID
           join role_permission rp on r.ID = rp.ROLE_ID
           join permission p on rp.PERMISSION_ID = p.id;

select r.name role, group_concat(p.name) permissions from
    role r join role_permission rp on r.id = rp.ROLE_ID
           join permission p on p.id = rp.PERMISSION_ID
group by r.name;

select
    u.id uer_id, u.USERNAME username, r.name role, p.permissions
from
    user u join user_role ur on u.id = ur.USER_ID join role r on r.ID = ur.ROLE_ID,
    (select r.name role, group_concat(p.name) permissions from
        role r join role_permission rp on r.id = rp.ROLE_ID
               join permission p on p.id = rp.PERMISSION_ID
     group by r.name) p
where
        r.name = p.role
;

insert into permission (NAME) values ('GRANT__PRIVILEGE'),('RESET__PASSWORD');
insert into role_permission (ROLE_ID, permission_id) values (1,9), (1,10);

CREATE VIEW VIEW_USER_PERMISSIONS as
select
    u.id uer_id, u.USERNAME username, r.name role, p.permissions
from
    user u join user_role ur on u.id = ur.USER_ID join role r on r.ID = ur.ROLE_ID,
    (select r.name role, group_concat(p.name) permissions from
        role r join role_permission rp on r.id = rp.ROLE_ID
               join permission p on p.id = rp.PERMISSION_ID
     group by r.name) p
where
        r.name = p.role
;

select * from VIEW_USER_PERMISSIONS;


