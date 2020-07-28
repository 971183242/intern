package com.demo.workshop.intern.app.service.impl;

import com.demo.workshop.intern.domain.profile.entity.Intern;
import com.demo.workshop.intern.domain.profile.entity.Team;
import com.demo.workshop.intern.domain.profile.entity.User;
import com.demo.workshop.intern.domain.profile.service.ProfileDomService;
import com.demo.workshop.intern.domain.report.service.MonthlySettlementDayRuleService;
import com.google.gson.Gson;
import com.demo.workshop.intern.app.service.ProfileAppService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Data
@Service
public class ProfileAppServiceImpl implements ProfileAppService {
    static Logger logger = LoggerFactory.getLogger(ProfileAppServiceImpl.class);

    @Autowired
    private ProfileDomService profileDomService;

    @Autowired
    private MonthlySettlementDayRuleService monthlySettlementDayRuleService;

    @Override
    public List<Team> findAllTeams() {
        return profileDomService.findAllTeams();
    }

    @Override
    public User createUser(User user) {
        logger.info("createUser. user:" + new Gson().toJson(user));
        return profileDomService.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        logger.info("updateUser. user:" + new Gson().toJson(user));
        return profileDomService.updateUser(user);
    }

    @Override
    public Optional<User> findUserByDomainId(String domainId) {
        return profileDomService.findUserByDomainId(domainId);
    }

    @Override
    public List<Intern> getInterns(Date date) {
        List<Date> dateWindow = monthlySettlementDayRuleService.getMonthlySettlementDateWindow(date);
        return profileDomService.findInterns(dateWindow.get(0), dateWindow.get(1));
    }

    @Override
    public Team findTeam(String userDomainId) {
        return profileDomService.findTeamByUserId(userDomainId);
    }
}
