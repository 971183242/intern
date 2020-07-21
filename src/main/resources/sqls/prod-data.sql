
-- 结算日
insert into T_SETTLEMENT_DAY(day) values(20);

-- teams
insert into T_TEAM(team_id,name, team_leader_id) values(''TMS'', ''tms'', ''LUAL'');
insert into T_TEAM(team_id,name, team_leader_id) values(''WMS/DCS'', ''WMS/DCS'',''YUANFR'');
insert into T_TEAM(team_id,name, team_leader_id) values(''IPS'', ''IPS'',''ZHUJO3'');
insert into T_TEAM(team_id,name, team_leader_id) values(''GPS'', ''GPS'', ''GEJO'');
insert into T_TEAM(team_id,name, team_leader_id) values(''FWK'', ''FWK'', ''DONGJA3'');

-- SuperAdmin
insert into t_user(domain_id,name,email,user_type,role,date_from, date_to,team_id)
values(''WUHO'',''HONG WU'',''hong.wu@oocl.com@oocl.com'',''EMPLOYEE'',''ROLE_SUPER_ADMIN'',null,null,null);
-- HR
insert into t_user(domain_id,name,email,user_type,role,date_from, date_to,team_id)
values(''WUWE'',''WENDY WU'',''wendy.wu@oocl.com'',''EMPLOYEE'',''ROLE_HR'',null,null,null);
-- TeamLeader
insert into t_user(domain_id,name,email,user_type,role,date_from, date_to,team_id)
values(''LUAL'',''ALEX LU'',''lual@oocl.com'',''EMPLOYEE'',''ROLE_TEAM_LEADER'',null,null,null);
insert into t_user(domain_id,name,email,user_type,role,date_from, date_to,team_id)
values(''YUANFR'',''FRANK YUAN'',''yuanfr@oocl.com'',''EMPLOYEE'',''ROLE_TEAM_LEADER'',null,null,null);
insert into t_user(domain_id,name,email,user_type,role,date_from, date_to,team_id)
values(''ZHUJO3'',''JOANN ZHU'',''zhujo3@oocl.com'',''EMPLOYEE'',''ROLE_TEAM_LEADER'',null,null,null);
insert into t_user(domain_id,name,email,user_type,role,date_from, date_to,team_id)
values(''GEJO'',''JOY GE'',''gejo@oocl.com'',''EMPLOYEE'',''ROLE_TEAM_LEADER'',null,null,null);
insert into t_user(domain_id,name,email,user_type,role,date_from, date_to,team_id)
values(''DONGJA3'',''JACKIE DONG'',''dongja3@oocl.com'',''EMPLOYEE'',''ROLE_TEAM_LEADER'',null,null,null);