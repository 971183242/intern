package com.oocl.workshop.intern.domain.profile.entity;

import lombok.Data;

@Data
public class Team {
    private String teamId;
    private String name;
    private TeamLeader teamLeader;
}
