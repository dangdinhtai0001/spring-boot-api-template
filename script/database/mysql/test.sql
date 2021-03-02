select *
from user;

select *
from user_role;

select *
from role;

select *
from role_permission;

select *
from permission;

select *
from VIEW_USER_PERMISSIONS
order by user_id;

select *
from view_role_permissions
order by role;

select u.id           id,
       u.USERNAME     username,
       u.EMAIL        email,
       u.PASSWORD     password,
       u.LOCKED       isLocked,
       u.ENABLED      isEnabled,
       up.roles       roles,
       up.permissions permissions
from user u
         join view_user_permissions up on u.ID = up.user_id where u.USERNAME = ? or u.EMAIL = ?;



commit;
