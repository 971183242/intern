-- 结算日
insert into T_SETTLEMENT_DAY(day) values(20);

-- teams
insert into T_TEAM(team_id,name, team_leader_id) values('TMS', 'TMS', 'LUAL');
insert into T_TEAM(team_id,name, team_leader_id) values('WMS/DCS', 'WMS/DCS','YUANFR');
insert into T_TEAM(team_id,name, team_leader_id) values('IPS', 'IPS','ZHUJO3');
insert into T_TEAM(team_id,name, team_leader_id) values('GPS', 'GPS', 'GEJO');
insert into T_TEAM(team_id,name, team_leader_id) values('FWK', 'FWK', 'DONGJA3');

-- SuperAdmin
insert into t_user(domain_id,name,email,role,date_from, date_to,team_id)
values('WUHO','HONG WU','zhangco2@oocl.com','ROLE_SUPER_ADMIN',null,null,null);

-- HR
insert into t_user(domain_id,name,email,role,date_from, date_to,team_id)
values('WUWE','WENDY WU','zhangco2@oocl.com','ROLE_HR',null,null,null);

-- TeamLeader
insert into t_user(domain_id,name,email,role,date_from, date_to,team_id)
values('DONGJA3','JACKIE DONG','zhangco2@oocl.com','ROLE_TEAM_LEADER',null,null,null);
insert into t_user(domain_id,name,email,role,date_from, date_to,team_id)
values('LUAL','LUAL','zhangco2@oocl.com','ROLE_TEAM_LEADER',null,null,null);
insert into t_user(domain_id,name,email,role,date_from, date_to,team_id)
values('ZHUJO3','ZHUJO3','zhangco2@oocl.com','ROLE_TEAM_LEADER',null,null,null);
insert into t_user(domain_id,name,email,role,date_from, date_to,team_id)
values('GEJO','GEJO','zhangco2@oocl.com','ROLE_TEAM_LEADER',null,null,null);
insert into t_user(domain_id,name,email,role,date_from, date_to,team_id)
values('YUANFR','YUANFR','zhangco2@oocl.com','ROLE_TEAM_LEADER',null,null,null);

-- INTERN
insert into t_user(domain_id,name,email,role,date_from, date_to,team_id)
values('ZHANGCO2','COLTEN ZHANG','colten.zhang@oocl.com','ROLE_INTERN','2020-07-01','2020-9-01','TMS');
insert into t_user(domain_id,name,email,role,date_from,date_to,team_id)
values('WANGWE4','WILL L Y WANG','will.l.y.wang@oocl.com','ROLE_INTERN','2020-07-01','2020-10-01','TMS');
insert into t_user(domain_id,name,email,role,date_from,date_to,team_id)
values('TANGZA','ZACK TANG','zack.tang@oocl.com','ROLE_INTERN','2020-07-01','2020-11-01','IPS');
insert into t_user(domain_id,name,email,role,date_from,date_to,team_id)
values('MIAOOY2','OYANG MIAO','oyang.miao@oocl.com','ROLE_INTERN','2020-07-01','2020-12-01','FWK');
insert into t_user(domain_id,name,email,role,date_from,date_to,team_id)
values('OUYANOB','OUYANG JUN','obed.ouyang@oocl.com','ROLE_INTERN','2020-07-01','2020-12-01','FWK');