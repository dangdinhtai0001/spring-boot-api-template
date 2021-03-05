select *
from view_user_permissions;

select *
from user;

select *
from permission;

select *
from role;

select *
from api_url;

select *
from api_url_permission;

select au.NAME name, au.URL url, group_concat(p.NAME) permissions
from api_url au
         join api_url_permission aup on au.ID = aup.URL_ID
         join permission p on aup.PERMISSION_ID = p.ID
group by au.id;


select au.NAME name, au.URL url, '' permissions
from api_url au
where au.id not in (select aup.URL_ID from api_url_permission aup);

update user
set locked  = 0,
    enabled = 1
where username = 'dangdinhtai';

commit;


select u.id           id,
       u.USERNAME     username,
       u.EMAIL        email,
       u.PASSWORD     password,
       u.LOCKED       isLocked,
       u.ENABLED      isEnabled,
       up.roles       roles,
       up.permissions permissions
from user u
         join view_user_permissions up on u.ID = up.user_id
where u.USERNAME = 'dangdinhtai'
   or u.EMAIL = 'dangdinhtai';

