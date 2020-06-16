package com.oocl.workshop.intern.domain.profile.service;

import com.oocl.workshop.intern.domain.profile.entity.*;
import com.oocl.workshop.intern.domain.profile.repository.po.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class ProfileFactory {

    public BaseUserPo createPo(User user) {
        if (user instanceof Intern) {
            return createInternPo((Intern) user);
        } else if (user instanceof TeamLeader) {
            return createTeamLeaderPo((TeamLeader) user);
        } else if (user instanceof HR) {
            return createHrPo((HR) user);
        }
        return null;
    }

    private void setCommonUserPoProperty(BaseUserPo baseUserPo, User user) {
        baseUserPo.setUserId(user.getUserId());
        baseUserPo.setDomainId(user.getDomainId());
        baseUserPo.setName(user.getUserName());
        baseUserPo.setEmail(user.getEmail());
    }

    private InternPo createInternPo(Intern intern) {
        InternPo internPo = new InternPo();
        setCommonUserPoProperty(internPo, intern);
        internPo.setPeriod(intern.getPeriod());
        return internPo;
    }

    private TeamLeaderPo createTeamLeaderPo(TeamLeader teamLeader) {
        TeamLeaderPo teamLeaderPo = new TeamLeaderPo();
        setCommonUserPoProperty(teamLeaderPo, teamLeader);
        return teamLeaderPo;
    }

    private HRPo createHrPo(HR hr) {
        HRPo hrPo = new HRPo();
        setCommonUserPoProperty(hrPo, hr);
        return hrPo;
    }


    public User getUser(BaseUserPo userPo) {
        if (userPo instanceof InternPo) {
            return createIntern((InternPo) userPo);
        } else if (userPo instanceof TeamLeaderPo) {
            return createTeamLeader((TeamLeaderPo) userPo);
        } else if (userPo instanceof HRPo) {
            return createHR((HRPo) userPo);
        } else if (userPo instanceof SuperAdminPo) {
            return createSuperAdmin((SuperAdminPo) userPo);
        }
        return null;
    }

    private void setCommonUserProperty(User user, BaseUserPo baseUserPo) {
        user.setUserId(baseUserPo.getUserId());
        user.setDomainId(baseUserPo.getDomainId());
        user.setUserName(baseUserPo.getName());
        user.setEmail(baseUserPo.getEmail());
    }

    private Intern createIntern(InternPo internPo) {
        Intern intern = new Intern();
        setCommonUserProperty(intern, internPo);
        intern.setPeriod(internPo.getPeriod());
        return intern;
    }

    private TeamLeader createTeamLeader(TeamLeaderPo teamLeaderPo) {
        TeamLeader teamLeader = new TeamLeader();
        setCommonUserProperty(teamLeader, teamLeaderPo);
        return teamLeader;
    }

    private HR createHR(HRPo hrPo) {
        HR hr = new HR();
        setCommonUserProperty(hr, hrPo);
        return hr;
    }

    private SuperAdmin createSuperAdmin(SuperAdminPo superAdminPo) {
        SuperAdmin superAdmin = new SuperAdmin();
        setCommonUserProperty(superAdmin, superAdminPo);
        return superAdmin;
    }

}
