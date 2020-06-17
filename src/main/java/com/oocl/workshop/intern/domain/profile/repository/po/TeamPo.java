package com.oocl.workshop.intern.domain.profile.repository.po;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "T_TEAM")
public class TeamPo {
    @Id
    private String teamId;

    private String name;

    private String teamLeaderId;

}
