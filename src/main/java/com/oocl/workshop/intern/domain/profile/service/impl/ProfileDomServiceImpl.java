package com.oocl.workshop.intern.domain.profile.service.impl;

import com.oocl.workshop.intern.domain.profile.entity.Intern;
import com.oocl.workshop.intern.domain.profile.entity.Team;
import com.oocl.workshop.intern.domain.profile.entity.User;
import com.oocl.workshop.intern.domain.profile.repository.facade.TeamRepo;
import com.oocl.workshop.intern.domain.profile.repository.facade.UserRepo;
import com.oocl.workshop.intern.domain.profile.repository.po.TeamPo;
import com.oocl.workshop.intern.domain.profile.repository.po.UserPo;
import com.oocl.workshop.intern.domain.profile.service.ProfileFactory;
import com.oocl.workshop.intern.domain.profile.service.ProfileDomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfileDomServiceImpl implements ProfileDomService {

    @Autowired
    private ProfileFactory profileFactory;

    @Autowired
    private TeamRepo teamRepo;

    @Autowired
    private UserRepo userRepo;

    @Override
    public List<Team> findAllTeams() {
        List<TeamPo> teamPoList = teamRepo.findAll();
        List<Team> teamList = teamPoList.stream()
                .map(profileFactory::getTeam)
                .collect(Collectors.toList());
        return teamList;
    }

    @Override
    public User createUser(User user) {
        UserPo userPo = profileFactory.createUserPo(user);
        userPo = userRepo.save(userPo);
        return profileFactory.getUser(userPo);
    }

    @Override
    public User updateUser(User user) {
        UserPo userPo = profileFactory.createUserPo(user);
        userPo = userRepo.save(userPo);
        return profileFactory.getUser(userPo);
    }

    @Override
    public Optional<User> findUserByDomainId(String domainId) {
        User user = userRepo.findById(domainId)
                .map(profileFactory::getUser)
                .orElse(null);
        return Optional.ofNullable(user);
    }

    @Override
    public List<Intern> findTeamInterns(String teamId, Date from, Date to) {
        List<UserPo> internPoList = userRepo.findTeamActiveInterns(teamId, from, to);
        List<Intern> internList = internPoList.stream()
                .map(profileFactory::getIntern)
                .collect(Collectors.toList());
        return internList;
    }

}