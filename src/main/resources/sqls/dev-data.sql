-- 结算日
insert into T_SETTLEMENT_DAY(day) values(20);

-- teams
insert into T_TEAM(team_id,name, team_leader_id) values('TeamA', 'TeamA', 'AMY');
insert into T_TEAM(team_id,name, team_leader_id) values('TeamB', 'TeamB','BOB');

-- SuperAdmin
insert into t_user(domain_id,name,email,role,date_from, date_to,team_id)
values('OLIVER','Oliver','oliver@oocl1.com','ROLE_SUPER_ADMIN',null,null,null);

-- HR
insert into t_user(domain_id,name,email,role,date_from, date_to,team_id)
values('WINDY','Windy','windy@oocl1.com','ROLE_HR',null,null,null);

-- TeamLeader
insert into t_user(domain_id,name,email,role,date_from, date_to,team_id)
values('AMY','Amy','amy@oocl1.com','ROLE_TEAM_LEADER',null,null,null);
insert into t_user(domain_id,name,email,role,date_from, date_to,team_id)
values('BOB','Bob','bob@oocl1.com','ROLE_TEAM_LEADER',null,null,null);

-- INTERN
insert into t_user(domain_id,name,email,role,date_from, date_to,team_id)
values('TOM','Tom','tom@oocl1.com','ROLE_INTERN','2020-07-01','2020-9-01','TeamA');
insert into t_user(domain_id,name,email,role,date_from,date_to,team_id)
values('JERRY','Jerry','jerry@oocl1.com','ROLE_INTERN','2020-07-01','2020-10-01','TeamA');
insert into t_user(domain_id,name,email,role,date_from,date_to,team_id)
values('DAVID','David','david@oocl1.com','ROLE_INTERN','2020-07-01','2020-11-01','TeamB');
insert into t_user(domain_id,name,email,role,date_from,date_to,team_id)
values('GARY','Gray','gary@oocl1.com','ROLE_INTERN','2020-07-01','2020-12-01','TeamB');