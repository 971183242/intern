package com.demo.workshop.intern.domain.profile.service;

import com.demo.workshop.intern.domain.profile.entity.*;
import com.demo.workshop.intern.domain.profile.entity.valueobject.InternPeriod;
import com.demo.workshop.intern.domain.profile.repository.facade.TeamRepo;
import com.demo.workshop.intern.domain.profile.repository.facade.UserRepo;
import com.demo.workshop.intern.domain.profile.repository.po.TeamPo;
import com.demo.workshop.intern.domain.profile.repository.po.UserPo;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileFactoryTest {
    @Mock
    TeamRepo teamRepo;
    @Mock
    UserRepo userRepo;
    @Captor
    ArgumentCaptor<String> stringCaptor;

    @InjectMocks
    ProfileFactory profileFactory;


    @Test
    public void createUserPo() {
        Employee leader = new Employee();
        leader.setDomainId("leader");
        leader.setEmail("leader@oocl.com");
        leader.setName("leader name");
        leader.setRoles(Lists.newArrayList(Role.TEAM_LEADER));
        UserPo leaderPo = profileFactory.createUserPo(leader);
        assertEquals(leader.getName(), leaderPo.getName());
        assertEquals(leader.getDomainId(), leaderPo.getDomainId());
        assertEquals(leader.getEmail(), leaderPo.getEmail());
        assertEquals(Role.TEAM_LEADER.getFullName(), leaderPo.getRole());


        Intern intern = new Intern();
        intern.setDomainId("intern_1");
        intern.setName("实习生乙");
        intern.setEmail("intern_1@oocl.com");
        intern.setRoles(Lists.newArrayList(Role.INTERN));
        InternPeriod period = new InternPeriod();
        period.setDateFrom(new Date(120, 1, 1));
        intern.setPeriod(period);

        Team team = new Team();
        team.setTeamId("tms");
        intern.setTeam(team);

        UserPo userPo = profileFactory.createUserPo(intern);
        assertEquals(intern.getDomainId(), userPo.getDomainId());
        assertEquals(intern.getName(), userPo.getName());
        assertEquals(intern.getEmail(), userPo.getEmail());
        assertEquals(Role.INTERN.getFullName(), userPo.getRole());
        Assertions.assertEquals(intern.getPeriod().getDateFrom(), userPo.getInternPeriod().getDateFrom());
        assertEquals(team.getTeamId(), userPo.getTeamId());
    }

    @Test
    void getUser() {
        UserPo teamLeaderPo = new UserPo();
        teamLeaderPo.setDomainId("teamLeader_01");
        teamLeaderPo.setEmail("teamLeader_01@oocl.com");
        teamLeaderPo.setName("XX项目负责人");
        teamLeaderPo.setRole(Role.TEAM_LEADER.getFullName());

        TeamPo teamPo01 = new TeamPo();
        teamPo01.setTeamId("team_01");
        teamPo01.setName("team_name_01");
        teamPo01.setTeamLeaderId(teamLeaderPo.getDomainId());

        // Intern - 指定了Team
        UserPo internPo0 = new UserPo();
        internPo0.setDomainId("intern_0");
        internPo0.setEmail("intern_0@oocl.com");
        internPo0.setName("实习生甲");
        internPo0.setRole(Role.INTERN.getFullName());
        internPo0.setInternPeriod(new InternPeriod(new Date(120, 0, 1), new Date(120, 5, 1)));
        internPo0.setTeamId(teamPo01.getTeamId());

        // Intern - 未指定了Team
        UserPo internPo1 = new UserPo();
        internPo1.setDomainId("intern_1");
        internPo1.setEmail("intern_1@oocl.com");
        internPo1.setName("实习生乙");
        internPo1.setRole(Role.INTERN.getFullName());
        internPo1.setInternPeriod(new InternPeriod(new Date(120, 0, 1), new Date(120, 5, 1)));

        when(teamRepo.findById(teamPo01.getTeamId())).thenReturn(Optional.of(teamPo01));
        when(userRepo.findById(teamLeaderPo.getDomainId())).thenReturn(Optional.of(teamLeaderPo));

        User user = profileFactory.getUser(teamLeaderPo);
        assertTrue(user instanceof Employee);
        assertEquals(teamLeaderPo.getDomainId(), user.getDomainId());
        assertEquals(teamLeaderPo.getName(), user.getName());
        assertEquals(teamLeaderPo.getEmail(), user.getEmail());
        assertEquals(Lists.newArrayList(Role.TEAM_LEADER), user.getRoles());

        Intern intern0 = (Intern) profileFactory.getUser(internPo0);
        assertEquals(internPo0.getDomainId(), intern0.getDomainId());
        assertEquals(internPo0.getName(), intern0.getName());
        assertEquals(internPo0.getEmail(), intern0.getEmail());
        Assertions.assertEquals(internPo0.getInternPeriod().getDateFrom(), intern0.getPeriod().getDateFrom());
        Assertions.assertEquals(internPo0.getInternPeriod().getDateTo(), intern0.getPeriod().getDateTo());
        assertEquals(teamPo01.getTeamId(), intern0.getTeam().getTeamId());
        assertEquals(teamPo01.getName(), intern0.getTeam().getName());
        assertEquals(teamPo01.getTeamLeaderId(), intern0.getTeam().getTeamLeader().getDomainId());
        assertEquals(teamLeaderPo.getDomainId(), intern0.getTeam().getTeamLeader().getDomainId());
        assertEquals(teamLeaderPo.getName(), intern0.getTeam().getTeamLeader().getName());
        assertEquals(teamLeaderPo.getEmail(), intern0.getTeam().getTeamLeader().getEmail());

        verify(teamRepo).findById(any());
        verify(userRepo).findById(any());


        reset(teamRepo);
        reset(userRepo);
        Intern intern1 = (Intern) profileFactory.getUser(internPo1);
        assertEquals(internPo1.getDomainId(), intern1.getDomainId());
        assertEquals(internPo1.getName(), intern1.getName());
        assertEquals(internPo1.getEmail(), intern1.getEmail());
        Assertions.assertEquals(internPo1.getInternPeriod().getDateFrom(), intern1.getPeriod().getDateFrom());
        Assertions.assertEquals(internPo1.getInternPeriod().getDateTo(), intern1.getPeriod().getDateTo());
        assertNull(intern1.getTeam());

        verify(teamRepo, never()).findById(any());
        verify(userRepo, never()).findById(any());
    }

    @Test
    void createTeamPo() {
        Team team = new Team();
        team.setName("team_name_02");
        team.setTeamId("team_02");
        Employee teamLeader = new Employee();
        teamLeader.setDomainId("leader_02");
        team.setTeamLeader(teamLeader);

        TeamPo teamPo = profileFactory.createTeamPo(team);
        assertEquals("team_02", teamPo.getTeamId());
        assertEquals("team_name_02", teamPo.getName());
        assertEquals("leader_02", teamPo.getTeamLeaderId());

        team.setTeamLeader(null);
        teamPo = profileFactory.createTeamPo(team);
        assertNull(teamPo.getTeamLeaderId());
    }


    @Test
    void getTeam() {
        UserPo teamLeaderPo = new UserPo();
        teamLeaderPo.setDomainId("teamLeader_01");
        teamLeaderPo.setEmail("teamLeader_01@oocl.com");
        teamLeaderPo.setName("XX项目负责人");
        teamLeaderPo.setRole(Role.TEAM_LEADER.getFullName());

        TeamPo teamPo01 = new TeamPo();
        teamPo01.setTeamId("team_01");
        teamPo01.setName("team_name_01");
        teamPo01.setTeamLeaderId(teamLeaderPo.getDomainId());

        when(userRepo.findById(anyString())).thenReturn(Optional.of(teamLeaderPo));

        Team team01 = profileFactory.getTeam(teamPo01);
        assertEquals("team_01", team01.getTeamId());
        assertEquals("team_name_01", team01.getName());
        assertEquals("teamLeader_01", team01.getTeamLeader().getDomainId());
        assertEquals("XX项目负责人", team01.getTeamLeader().getName());
        assertEquals("teamLeader_01@oocl.com", team01.getTeamLeader().getEmail());

        verify(userRepo).findById(stringCaptor.capture());
        assertEquals(teamLeaderPo.getDomainId(), stringCaptor.getValue());
        reset(userRepo);

        TeamPo teamPo02 = new TeamPo();
        teamPo02.setTeamId("team_02");
        teamPo02.setName("team_name_02");
        Team team02 = profileFactory.getTeam(teamPo02);
        assertEquals(teamPo02.getTeamId(), team02.getTeamId());
        assertEquals(teamPo02.getName(), team02.getName());
        assertNull(team02.getTeamLeader());

        verify(userRepo, never()).findById(any());
    }


}