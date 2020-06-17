package com.oocl.workshop.intern.domain.profile.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TeamLeader extends User {
    private List<Team> teamList = new ArrayList<>();
}
