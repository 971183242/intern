package com.oocl.workshop.intern.domain.profile.service;

import com.oocl.workshop.intern.domain.profile.entity.*;
import com.oocl.workshop.intern.domain.profile.repository.facade.TeamRepo;
import com.oocl.workshop.intern.domain.profile.repository.facade.UserRepo;
import com.oocl.workshop.intern.domain.profile.repository.po.TeamPo;
import com.oocl.workshop.intern.domain.profile.repository.po.UserPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProfileFactory {

    private static final String SYMBOL_ROLE_SEPARATOR = ";";

    @Autowired
    UserRepo userRepo;

    @Autowired
    TeamRepo teamRepo;

    public UserPo createUserPo(User user) {
        if (user instanceof Intern) {
            return createUserPo((Intern) user);
        } else if (user instanceof Employee) {
            return createUserPo((Employee) user);
        }
        return null;
    }

    private UserPo createUserPo(User user, UserType userType) {
        UserPo userPo = new UserPo();
        userPo.setDomainId(user.getDomainId());
        userPo.setName(user.getName());
        userPo.setEmail(user.getEmail());
        userPo.setUserType(userType);
        userPo.setRole(user.getRoles().stream().map(r -> r.getFullName()).collect(Collectors.joining(SYMBOL_ROLE_SEPARATOR)));
        return userPo;
    }


    private UserPo createUserPo(Intern intern) {
        UserPo userPo = createUserPo(intern, UserType.INTERN);
        userPo.setInternPeriod(intern.getPeriod());
        userPo.setTeamId(intern.getTeam() != null ? intern.getTeam().getTeamId() : null);
        return userPo;
    }

    private UserPo createUserPo(Employee employee) {
        return createUserPo(employee, UserType.EMPLOYEE);
    }


    public User getUser(UserPo userPo) {
        if (UserType.INTERN.equals(userPo.getUserType())) {
            return getIntern(userPo);
        } else if (UserType.EMPLOYEE.equals(userPo.getUserType())) {
            return getEmployee(userPo);
        }
        return null;
    }


    public Intern getIntern(UserPo userPo) {
        if (!UserType.INTERN.equals(userPo.getUserType())) {
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

    public Employee getEmployee(UserPo userPo) {
        if (!UserType.EMPLOYEE.equals(userPo.getUserType())) {
            return null;
        }
        Employee employee = new Employee();
        setCommonUserProperty(employee, userPo);
        return employee;
    }


    private void setCommonUserProperty(User user, UserPo userPo) {
        user.setDomainId(userPo.getDomainId());
        user.setName(userPo.getName());
        user.setEmail(userPo.getEmail());

        if (!StringUtils.isEmpty(userPo.getRole())) {
            user.setRoles(Stream.of(userPo.getRole().split(SYMBOL_ROLE_SEPARATOR)).map(Role::getRole).collect(Collectors.toList()));
        } else {
            user.getRoles().clear();
        }
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


    private Employee getTeamLeader(Optional<UserPo> userPo) {
        return userPo.isPresent() ? (Employee) getUser(userPo.get()) : null;
    }
}
