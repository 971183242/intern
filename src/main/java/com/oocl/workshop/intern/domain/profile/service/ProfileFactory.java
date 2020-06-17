package com.oocl.workshop.intern.domain.profile.service;

import com.oocl.workshop.intern.domain.profile.entity.*;
import com.oocl.workshop.intern.domain.profile.repository.po.*;
import org.springframework.stereotype.Service;

@Service
public class ProfileFactory {

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
        userPo.setUserId(user.getUserId());
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
        return userPo;
    }


    public User getUser(UserPo userPo) {
        if (UserType.Intern.equals(userPo.getUserType())) {
            return createIntern(userPo);
        } else if (UserType.TeamLeader.equals(userPo.getUserType())) {
            return createTeamLeader(userPo);
        } else if (UserType.HR.equals(userPo.getUserType())) {
            return createHR(userPo);
        } else if (UserType.SuperAdmin.equals(userPo.getUserType())) {
            return createSuperAdmin(userPo);
        }
        return null;
    }

    private void setCommonUserProperty(User user, UserPo userPo) {
        user.setUserId(userPo.getUserId());
        user.setDomainId(userPo.getDomainId());
        user.setUserName(userPo.getName());
        user.setEmail(userPo.getEmail());
    }

    private Intern createIntern(UserPo userPo) {
        Intern intern = new Intern();
        setCommonUserProperty(intern, userPo);
        intern.setPeriod(userPo.getInternPeriod());
        return intern;
    }

    private TeamLeader createTeamLeader(UserPo userPo) {
        TeamLeader teamLeader = new TeamLeader();
        setCommonUserProperty(teamLeader, userPo);
        return teamLeader;
    }

    private HR createHR(UserPo userPo) {
        HR hr = new HR();
        setCommonUserProperty(hr, userPo);
        return hr;
    }

    private SuperAdmin createSuperAdmin(UserPo userPo) {
        SuperAdmin superAdmin = new SuperAdmin();
        setCommonUserProperty(superAdmin, userPo);
        return superAdmin;
    }

}
