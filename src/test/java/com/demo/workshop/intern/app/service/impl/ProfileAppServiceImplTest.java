package com.demo.workshop.intern.app.service.impl;

import com.demo.workshop.intern.domain.profile.entity.Employee;
import com.demo.workshop.intern.domain.profile.entity.Intern;
import com.demo.workshop.intern.domain.profile.entity.Team;
import com.demo.workshop.intern.domain.profile.entity.User;
import com.demo.workshop.intern.domain.profile.service.ProfileDomService;
import com.demo.workshop.intern.domain.report.service.MonthlySettlementDayRuleService;
import com.google.common.collect.Lists;
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
        verify(profileDomService).findAllTeams();
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
        verify(profileDomService).createUser(user);
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
        verify(profileDomService).updateUser(user);
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
        verify(profileDomService).findUserByDomainId(any());
    }


    @Test
    void getInterns() {
        Date date = new Date();
        when(monthlySettlementDayRuleService.getMonthlySettlementDateWindow(date)).thenReturn(Lists.newArrayList(new Date(), new Date()));
        when(profileDomService.findInterns(any(), any())).thenReturn(Lists.newArrayList(new Intern()));

        List<Intern> interns = profileAppServiceImpl.getInterns(date);
        assertEquals(1, interns.size());
        verify(profileDomService).findInterns(any(Date.class), any(Date.class));
    }
}