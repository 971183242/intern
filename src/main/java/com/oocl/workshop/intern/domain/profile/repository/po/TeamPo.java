package com.oocl.workshop.intern.domain.profile.repository.po;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class TeamPo {
    @Id
    private Long teamId;

    private String name;

    private Long teamLeaderId;

}
