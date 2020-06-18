package com.oocl.workshop.intern.domain.profile.service;

import com.oocl.workshop.intern.domain.profile.entity.Team;

import java.util.List;

public interface TeamProfileService {
    List<Team> findAllTeams();
}
