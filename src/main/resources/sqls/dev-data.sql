-- 结算日
insert into T_SETTLEMENT_DAY(day) values(20);

-- teams
insert into T_TEAM(team_id,name, team_leader_id) values('TMS', 'tms', 'GUDA');
insert into T_TEAM(team_id,name, team_leader_id) values('IPS', 'ips',null);
insert into T_TEAM(team_id,name, team_leader_id) values('FW', 'FW','DONGJA3');
insert into T_TEAM(team_id,name, team_leader_id) values('WMS', 'WMS','CUIST');
insert into T_TEAM(team_id,name, team_leader_id) values('GPS', 'GPS', null);
insert into T_TEAM(team_id,name, team_leader_id) values('HR', 'HR', null);

-- SuperAdmin
insert into t_user(domain_id,name,email,user_type,role,date_from, date_to,team_id)
values('WUHO','HONG WU','hong.wu@oocl.com@oocl.com','EMPLOYEE','ROLE_SUPER_ADMIN',null,null,null);

-- HR
insert into t_user(domain_id,name,email,user_type,role,date_from, date_to,team_id)
values('WUWE','WENDY WU','wendy.wu@oocl.com','EMPLOYEE','ROLE_HR',null,null,null);

-- TeamLeader
insert into t_user(domain_id,name,email,user_type,role,date_from, date_to,team_id)
values('DONGJA3','JACKIE DONG','jackie.dong@oocl.com','EMPLOYEE','ROLE_TEAM_LEADER',null,null,null);
insert into t_user(domain_id,name,email,user_type,role,date_from, date_to,team_id)
values('CUIST','STIKE CUI','stike.cui@oocl.com','EMPLOYEE','ROLE_TEAM_LEADER',null,null,null);
insert into t_user(domain_id,name,email,user_type,role,date_from, date_to,team_id)
values('GUDA','DAVE GU','dave.gu@oocl.com','EMPLOYEE','ROLE_TEAM_LEADER',null,null,null);

-- INTERN
insert into t_user(domain_id,name,email,user_type,role,date_from, date_to,team_id)
values('ZHANGCO2','COLTEN ZHANG','colten.zhang@oocl.com','INTERN','ROLE_INTERN','2020-07-01','2020-9-01','TMS');
insert into t_user(domain_id,name,email,user_type,role,date_from,date_to,team_id)
values('WANGWE4','WILL L Y WANG','will.l.y.wang@oocl.com','INTERN','ROLE_INTERN','2020-07-01','2020-10-01','TMS');
insert into t_user(domain_id,name,email,user_type,role,date_from,date_to,team_id)
values('TANGZA','ZACK TANG','zack.tang@oocl.com','INTERN','ROLE_INTERN','2020-07-01','2020-11-01','IPS');
insert into t_user(domain_id,name,email,user_type,role,date_from,date_to,team_id)
values('MIAOOY2','OYANG MIAO','oyang.miao@oocl.com','INTERN','ROLE_INTERN','2020-07-01','2020-12-01','FW');
insert into t_user(domain_id,name,email,user_type,role,date_from,date_to,team_id)
values('OUYANOB','OUYANG JUN','obed.ouyang@oocl.com','INTERN','ROLE_INTERN','2020-07-01','2020-12-01','FW');