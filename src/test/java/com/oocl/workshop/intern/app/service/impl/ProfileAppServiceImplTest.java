package com.oocl.workshop.intern.app.service.impl;

import com.google.common.collect.Lists;
import com.oocl.workshop.intern.domain.profile.entity.Employee;
import com.oocl.workshop.intern.domain.profile.entity.Intern;
import com.oocl.workshop.intern.domain.profile.entity.Team;
import com.oocl.workshop.intern.domain.profile.entity.User;
import com.oocl.workshop.intern.domain.profile.service.ProfileDomService;
import com.oocl.workshop.intern.domain.report.service.MonthlySettlementDayRuleService;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Log
@ExtendWith(MockitoExtension.class)
class ProfileAppServiceImplTest {
    @Mock
    ProfileDomService profileDomService;

    @Mock
    MonthlySettlementDayRuleService monthlySettlementDayRuleService;

    @InjectMocks
    ProfileAppServiceImpl profileAppServiceImpl;

    @Test
    void findAllTeams() {
        ArrayList<Team> teams = Lists.newArrayList(new Team());
        when(profileDomService.findAllTeams()).thenReturn(teams);

        List<Team> allTeams = profileAppServiceImpl.findAllTeams();
        assertArrayEquals(teams.toArray(), allTeams.toArray());
        verify(profileDomService, times(1)).findAllTeams();
    }

    @Test
    void createUser() {
        User user = new Employee();
        user.setName("userName");
        user.setDomainId("domainId");

        when(profileDomService.createUser(user)).thenReturn(user);

        User savedUser = profileAppServiceImpl.createUser(user);

        assertEquals(user.getDomainId(), savedUser.getDomainId());
        assertEquals(user.getName(), savedUser.getName());
        verify(profileDomService, times(1)).createUser(user);
    }

    @Test
    void updateUser() {
        User user = new Employee();
        user.setName("userName");
        user.setDomainId("domainId");

        when(profileDomService.updateUser(user)).thenReturn(user);

        User savedUser = profileAppServiceImpl.updateUser(user);

        assertEquals(user.getDomainId(), savedUser.getDomainId());
        assertEquals(user.getName(), savedUser.getName());
        verify(profileDomService, times(1)).updateUser(user);
    }

    @Test
    void findUserByDomainId() {
        String domainId = "testUserId";

        Employee employee = new Employee();
        employee.setDomainId(domainId);
        employee.setName("test");
        when(profileDomService.findUserByDomainId(domainId)).thenReturn(Optional.of(employee));

        Optional<User> userByDomainId = profileAppServiceImpl.findUserByDomainId(domainId);
        assertEquals(employee.getDomainId(), userByDomainId.get().getDomainId());
        assertEquals(employee.getName(), userByDomainId.get().getName());
        verify(profileDomService, times(1)).findUserByDomainId(any());
    }

    @Test
    void findTeamInterns() {
        Date date = new Date();
        Date from = new Date(120, 0, 21);
        Date to = new Date(120, 1, 20);

        when(monthlySettlementDayRuleService.getMonthlySettlementDateWindow(date)).thenReturn(Lists.newArrayList(from, to));
        when(profileDomService.findTeamInterns(anyString(), any(), any())).thenReturn(Lists.newArrayList(new Intern()));

        List<Intern> teamInterns = profileAppServiceImpl.findTeamInterns("teamId", date);
        assertEquals(1, teamInterns.size());

        verify(profileDomService, times(1)).findTeamInterns("teamId", from, to);
        verify(monthlySettlementDayRuleService, times(1)).getMonthlySettlementDateWindow(any());
    }

    @Test
    void getInterns() {
        Date date = new Date();
        when(monthlySettlementDayRuleService.getMonthlySettlementDateWindow(date)).thenReturn(Lists.newArrayList(new Date(), new Date()));
        when(profileDomService.findInterns(any(), any())).thenReturn(Lists.newArrayList(new Intern()));

        List<Intern> interns = profileAppServiceImpl.getInterns(date);
        assertEquals(1, interns.size());
        verify(profileDomService, times(1)).findInterns(any(Date.class), any(Date.class));
    }
}