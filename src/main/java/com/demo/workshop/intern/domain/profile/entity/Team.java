package com.demo.workshop.intern.domain.profile.entity;

import lombok.Data;

@Data
public class Team {
    private String teamId;
    private String name;
    private Employee teamLeader;
}
