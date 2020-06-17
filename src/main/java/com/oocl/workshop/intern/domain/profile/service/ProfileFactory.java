package com.oocl.workshop.intern.domain.profile.service;

import com.oocl.workshop.intern.domain.profile.entity.*;
import com.oocl.workshop.intern.domain.profile.repository.facade.TeamRepo;
import com.oocl.workshop.intern.domain.profile.repository.facade.UserRepo;
import com.oocl.workshop.intern.domain.profile.repository.po.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class ProfileFactory {

    @Autowired
    UserRepo userRepo;

    @Autowired
    TeamRepo teamRepo;

    public UserPo createPo(User user) {
        if (user instanceof Intern) {
            return createUserPo((Intern) user);
        } else if (user instanceof TeamLeader) {
            return createUserPo(user, UserType.TeamLeader);
        } else if (user instanceof HR) {
            return createUserPo(user, UserType.HR);
        } else if (user instanceof SuperAdmin) {
            return createUserPo(user, UserType.SuperAdmin);
        }
        return null;
    }


    private UserPo createUserPo(User user) {
        UserPo userPo = new UserPo();
        userPo.setDomainId(user.getDomainId());
        userPo.setName(user.getUserName());
        userPo.setEmail(user.getEmail());
        return userPo;
    }


    private UserPo createUserPo(User user, UserType userType) {
        UserPo userPo = createUserPo(user);
        userPo.setUserType(userType);
        return userPo;
    }


    private UserPo createUserPo(Intern intern) {
        UserPo userPo = createUserPo(intern, UserType.Intern);
        userPo.setInternPeriod(intern.getPeriod());
        userPo.setTeamId(intern.getTeam() != null ? intern.getTeam().getTeamId() : null);
        return userPo;
    }


    public User getUser(UserPo userPo) {
        if (UserType.Intern.equals(userPo.getUserType())) {
            return getIntern(userPo);
        } else if (UserType.TeamLeader.equals(userPo.getUserType())) {
            return getTeamLeader(userPo);
        } else if (UserType.HR.equals(userPo.getUserType())) {
            return getHR(userPo);
        } else if (UserType.SuperAdmin.equals(userPo.getUserType())) {
            return getSuperAdmin(userPo);
        }
        return null;
    }


    public Intern getIntern(UserPo userPo) {
        if (!UserType.Intern.equals(userPo.getUserType())) {
            return null;
        }
        Intern intern = new Intern();
        setCommonUserProperty(intern, userPo);
        intern.setPeriod(userPo.getInternPeriod());
        if (!StringUtils.isEmpty(userPo.getTeamId())) {
            intern.setTeam(getTeam(teamRepo.findById(userPo.getTeamId())));
        }
        return intern;
    }


    public TeamLeader getTeamLeader(UserPo userPo) {
        if (!UserType.TeamLeader.equals(userPo.getUserType())) {
            return null;
        }
        TeamLeader teamLeader = new TeamLeader();
        setCommonUserProperty(teamLeader, userPo);
        return teamLeader;
    }


    public HR getHR(UserPo userPo) {
        if (!UserType.HR.equals(userPo.getUserType())) {
            return null;
        }
        HR hr = new HR();
        setCommonUserProperty(hr, userPo);
        return hr;
    }


    public SuperAdmin getSuperAdmin(UserPo userPo) {
        if (!UserType.SuperAdmin.equals(userPo.getUserType())) {
            return null;
        }
        SuperAdmin superAdmin = new SuperAdmin();
        setCommonUserProperty(superAdmin, userPo);
        return superAdmin;
    }


    private void setCommonUserProperty(User user, UserPo userPo) {
        user.setDomainId(userPo.getDomainId());
        user.setUserName(userPo.getName());
        user.setEmail(userPo.getEmail());
    }


    public TeamPo createTeamPo(Team team) {
        TeamPo teamPo = new TeamPo();
        teamPo.setTeamId(team.getTeamId());
        teamPo.setName(team.getName());
        if (!StringUtils.isEmpty(team.getTeamLeader())) {
            teamPo.setTeamLeaderId(team.getTeamLeader().getDomainId());
        }
        return teamPo;
    }


    private Team getTeam(Optional<TeamPo> teamPoOptional) {
        return teamPoOptional.isPresent() ? getTeam(teamPoOptional.get()) : null;
    }


    public Team getTeam(TeamPo teamPo) {
        Team team = new Team();
        team.setTeamId(teamPo.getTeamId());
        team.setName(teamPo.getName());
        if (!StringUtils.isEmpty(teamPo.getTeamLeaderId())) {
            team.setTeamLeader(getTeamLeader(userRepo.findById(teamPo.getTeamLeaderId())));
        }
        return team;
    }


    private TeamLeader getTeamLeader(Optional<UserPo> userPo) {
        return userPo.isPresent() ? getTeamLeader(userPo.get()) : null;
    }
}
