package com.oocl.workshop.intern.domain.profile.service.impl;

import com.oocl.workshop.intern.domain.profile.entity.Intern;
import com.oocl.workshop.intern.domain.profile.entity.TeamLeader;
import com.oocl.workshop.intern.domain.profile.entity.User;
import com.oocl.workshop.intern.domain.profile.entity.UserType;
import com.oocl.workshop.intern.domain.profile.repository.facade.TeamRepo;
import com.oocl.workshop.intern.domain.profile.repository.facade.UserRepo;
import com.oocl.workshop.intern.domain.profile.repository.po.TeamPo;
import com.oocl.workshop.intern.domain.profile.repository.po.UserPo;
import com.oocl.workshop.intern.domain.profile.service.ProfileDomainService;
import com.oocl.workshop.intern.domain.profile.service.ProfileFactory;
import com.oocl.workshop.intern.infrastructure.common.entity.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class ProfileDomainServiceImpl implements ProfileDomainService {

    @Autowired
    private ProfileFactory profileFactory;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TeamRepo teamRepo;

    @Override
    public Intern createIntern(Intern intern) {
        UserPo internPo = profileFactory.createPo(intern);
        internPo = userRepo.save(internPo);
        return (Intern) profileFactory.getUser(internPo);
    }

    @Override
    public Intern deleteIntern(Intern intern) {
        UserPo userPo = profileFactory.createPo(intern);
        userRepo.delete(userPo);
        return intern;
    }

    @Override
    public Intern updateIntern(Intern intern) {
        Objects.requireNonNull(intern.getUserId(), "Intern id should not be empty in update");
        UserPo userPo = profileFactory.createPo(intern);
        userRepo.save(userPo);
        return intern;
    }

    @Override
    public Optional<User> findUserByDomainId(String domainId) {
        Objects.requireNonNull(domainId, "DomainId should not be empty");
        UserPo userPo = userRepo.findUserByDomainId(domainId);
        User user = Optional.ofNullable(userPo)
                .map(profileFactory::getUser)
                .orElse(null);
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<TeamLeader> findTeamLeaderByDomainId(String domainId) {
        Objects.requireNonNull(domainId, "DomainId should not be empty");
        UserPo userPo = userRepo.findUserByDomainId(domainId);
        if (userPo == null || !UserType.TeamLeader.equals(userPo.getUserType())) {
            return Optional.empty();
        }
        Optional<TeamPo> teamPoOpt = teamRepo.findByTeamLeaderId(userPo.getUserId());
        TeamLeader teamLeader = (TeamLeader) profileFactory.getUser(userPo);
        teamPoOpt.map(profileFactory::createTeam)
                .map(teamLeader.getTeamList()::add)
                .orElse(null);

        return Optional.of(teamLeader);
    }

    @Override
    public Page<Intern> findInternByTeamId(Long teamId, PageInfo pageInfo) {
        Optional<TeamPo> teamPoOpt = teamRepo.findById(teamId);
        Page<UserPo> userPoPage = userRepo.findByUserTypeAndTeam(UserType.Intern, teamPoOpt.orElse(null),
                PageRequest.of(pageInfo.getPageIndex(), pageInfo.getPageSize()));
        return userPoPage.map(profileFactory::getIntern);
    }

    @Override
    public Page<Intern> findActiveInternByTeamId(Long teamId, PageInfo pageInfo) {
        Optional<TeamPo> teamPoOpt = teamRepo.findById(teamId);
        Page<UserPo> userPoPage = userRepo.findActiveInternByTeam(teamPoOpt.orElse(null),
                PageRequest.of(pageInfo.getPageIndex(), pageInfo.getPageSize()));
        return userPoPage.map(profileFactory::getIntern);
    }

    @Override
    public Page<Intern> findIntern(PageInfo pageInfo) {
        PageRequest.of(pageInfo.getPageIndex(), pageInfo.getPageSize());
        Page<UserPo> userPoPage = userRepo.findByUserType(UserType.Intern, PageRequest.of(pageInfo.getPageIndex(), pageInfo.getPageSize()));
        return userPoPage.map(profileFactory::getIntern);
    }
}
