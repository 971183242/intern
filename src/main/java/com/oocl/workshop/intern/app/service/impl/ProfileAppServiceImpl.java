package com.oocl.workshop.intern.app.service.impl;

import com.oocl.workshop.intern.app.service.ProfileAppService;
import com.oocl.workshop.intern.domain.profile.entity.Intern;
import com.oocl.workshop.intern.domain.profile.entity.Role;
import com.oocl.workshop.intern.domain.profile.entity.Team;
import com.oocl.workshop.intern.domain.profile.entity.User;
import com.oocl.workshop.intern.domain.profile.service.ProfileDomService;
import com.oocl.workshop.intern.domain.report.repostitory.facade.MonthlySettlementDayRuleRepo;
import com.oocl.workshop.intern.domain.report.service.MonthlySettlementDayRuleService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Service
public class ProfileAppServiceImpl  implements ProfileAppService {
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
        return profileDomService.createUser(user);
    }

    @Override
    public User updateUser(User user) {
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
