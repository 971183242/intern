package com.demo.workshop.intern.domain.profile.repository.po;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "T_TEAM", indexes = @Index(name = "idx_team_leader_id", columnList = "team_leader_id"))
public class TeamPo {
    @Id
    private String teamId;

    private String name;

    @Column(name = "team_leader_id")
    private String teamLeaderId;

}
