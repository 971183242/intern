package com.oocl.workshop.intern.domain.profile.service.impl;

import com.google.gson.Gson;
import com.oocl.workshop.intern.domain.profile.entity.*;
import com.oocl.workshop.intern.domain.profile.repository.facade.TeamRepo;
import com.oocl.workshop.intern.domain.profile.repository.facade.UserRepo;
import com.oocl.workshop.intern.domain.profile.repository.po.TeamPo;
import com.oocl.workshop.intern.domain.profile.repository.po.UserPo;
import com.oocl.workshop.intern.domain.profile.service.ProfileFactory;
import com.oocl.workshop.intern.domain.profile.service.ProfileDomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfileDomServiceImpl implements ProfileDomService {
    static Logger logger = LoggerFactory.getLogger(ProfileDomServiceImpl.class);

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
        logger.debug("findAllTeams:" + new Gson().toJson(teamList));
        return teamList;
    }

    @Override
    public User createUser(User user) {
        logger.info("create user:" + new Gson().toJson(user));
        UserPo userPo = profileFactory.createUserPo(user);
        userPo = userRepo.save(userPo);
        logger.info("saved userPo:" + new Gson().toJson(userPo));
        return profileFactory.getUser(userPo);
    }

    @Override
    public User updateUser(User user) {
        logger.info("update user:" + new Gson().toJson(user));
        UserPo userPo = profileFactory.createUserPo(user);
        userPo = userRepo.save(userPo);
        logger.info("updated userPo:" + new Gson().toJson(userPo));
        return profileFactory.getUser(userPo);
    }

    @Override
    public Optional<User> findUserByDomainId(String domainId) {
        logger.info("findUserByDomainId domainId:" + domainId);
        User user = userRepo.findById(domainId)
                .map(profileFactory::getUser)
                .orElse(null);
        return Optional.ofNullable(user);
    }

    @Override
    public List<Intern> findTeamInterns(String teamId, Date from, Date to) {
        logger.info(String.format("findTeamInterns. teamId:%s, from:%s, to:%s", teamId, from, to));
        List<UserPo> internPoList = userRepo.findTeamActiveInterns(teamId, from, to);
        List<Intern> internList = internPoList.stream()
                .map(profileFactory::getIntern)
                .collect(Collectors.toList());
        logger.debug("findTeamInterns:" + new Gson().toJson(internList));
        return internList;
    }

    @Override
    public List<Intern> findInterns(Date from, Date to) {
        logger.info(String.format("findInterns. from:%s, to:%s", from, to));
        List<UserPo> internPoList = userRepo.findActiveInterns(from, to);
        logger.info(String.format("findInterns. size:%s", internPoList.size()));
        return internPoList.stream().map(profileFactory::getIntern).collect(Collectors.toList());
    }

    @Override
    public Team findTeamByUserId(String domainId) {
        Optional<UserPo> userPo = userRepo.findById(domainId);
        if (!userPo.isPresent()) {
            return null;
        }
        Optional<TeamPo> teamPoOptional;
        if (Role.isIntern(userPo.get().getRole()) && !StringUtils.isEmpty(userPo.get().getTeamId())) {
            teamPoOptional = teamRepo.findById(userPo.get().getTeamId());

        } else {
            teamPoOptional = teamRepo.findFirstByTeamLeaderId(userPo.get().getDomainId());
        }
        return teamPoOptional.isPresent() ? profileFactory.getTeam(teamPoOptional.get()) : null;
    }

    @Override
    public List<User> findUserByRole(Role role) {
        List<UserPo> userPoList = userRepo.findByRoleContains(role.getFullName());
        return userPoList.stream().map(profileFactory::getUser).collect(Collectors.toList());
    }
}
