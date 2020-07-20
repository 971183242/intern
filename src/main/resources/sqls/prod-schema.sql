CREATE TABLE t_settlement_day (
    DAY INTEGER NOT NULL,
    PRIMARY KEY (DAY)
);

CREATE TABLE t_team (
    team_id VARCHAR(255) NOT NULL,
    NAME VARCHAR(255),
    team_leader_id VARCHAR(255),
    PRIMARY KEY (team_id)
);

CREATE TABLE t_user (
    domain_id VARCHAR(255) NOT NULL,
    is_active BIT DEFAULT TRUE,
    email VARCHAR(255),
    date_from DATE,
    date_to DATE,
    name VARCHAR(255),
    role VARCHAR(255),
    team_id VARCHAR(255),
    user_type VARCHAR(255),
    PRIMARY KEY (domain_id)
);

CREATE TABLE t_attendance (
    attendance_id BIGINT NOT NULL AUTO_INCREMENT,
    created_by VARCHAR(255),
    created_date DATE,
    last_modified_by VARCHAR(255),
    last_modified_date DATE,
    version INTEGER NOT NULL,
    attendance_status VARCHAR(255),
    intern_id VARCHAR(255),
    work_day DATE,
    PRIMARY KEY (attendance_id)
);

create index idx_team_leader_id on t_team (team_leader_id);
create index idx_user_team_id on t_user (team_id);
create index idx_attendace_day on t_attendance (work_day);
alter table t_attendance add constraint idx_attendance_intern_day unique (intern_id, work_day);

