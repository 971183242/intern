-- Super Admin
insert into t_user(domain_id,name,email,user_type,role,date_from, date_to,team_id)
values('superadmin','超级管理员','superadmin@oocl.com','EMPLOYEE','ROLE_SUPER_ADMIN',null,null,null);

-- HR
insert into t_user(domain_id,name,email,user_type,role,date_from, date_to,team_id)
values('hr','人事经理','hr@oocl.com','EMPLOYEE','ROLE_HR',null,null,null);

-- TeamLeader
insert into t_user(domain_id,name,email,user_type,role,date_from, date_to,team_id)
values('teamLeader','teamLeader','teamLeader@oocl.com','EMPLOYEE','ROLE_TEAM_LEADER',null,null,null);

-- INTERN
insert into t_user(domain_id,name,email,user_type,role,date_from, date_to,team_id)
values('intern_0','实习生甲','intern_0@oocl.com','INTERN','ROLE_INTERN','2020-01-01','2020-06-01',null);
insert into t_user(domain_id,name,email,user_type,role,date_from,date_to,team_id)
values('intern_1','实习生乙','intern_1@oocl.com','INTERN','ROLE_INTERN','2020-02-01','2020-06-01','TMS-TEST');
insert into t_user(domain_id,name,email,user_type,role,date_from,date_to,team_id)
values('ouyanob','欧阳俊','intern_1@oocl.com','INTERN','ROLE_INTERN','2020-02-01','2020-06-01','TMS');
