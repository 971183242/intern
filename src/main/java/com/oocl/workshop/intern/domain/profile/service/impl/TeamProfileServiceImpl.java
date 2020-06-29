package com.oocl.workshop.intern.domain.profile.service.impl;

import com.oocl.workshop.intern.domain.profile.entity.Team;
import com.oocl.workshop.intern.domain.profile.repository.facade.TeamRepo;
import com.oocl.workshop.intern.domain.profile.repository.po.TeamPo;
import com.oocl.workshop.intern.domain.profile.service.ProfileFactory;
import com.oocl.workshop.intern.domain.profile.service.TeamProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamProfileServiceImpl implements TeamProfileService {

    @Autowired
    private ProfileFactory profileFactory;

    @Autowired
    private TeamRepo teamRepo;

    @Override
    public List<Team> findAllTeams() {
        List<TeamPo> teamPoList = teamRepo.findAll();
        List<Team> teamList = teamPoList.stream()
                .map(profileFactory::getTeam)
                .collect(Collectors.toList());
        return teamList;
    }
}
