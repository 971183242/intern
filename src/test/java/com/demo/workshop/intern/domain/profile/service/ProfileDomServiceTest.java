package com.demo.workshop.intern.domain.profile.service;

import com.demo.workshop.intern.domain.profile.entity.*;
import com.demo.workshop.intern.domain.profile.repository.facade.TeamRepo;
import com.demo.workshop.intern.domain.profile.repository.facade.UserRepo;
import com.demo.workshop.intern.domain.profile.repository.po.TeamPo;
import com.demo.workshop.intern.domain.profile.repository.po.UserPo;
import com.demo.workshop.intern.domain.profile.service.impl.ProfileDomServiceImpl;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileDomServiceTest {

    @InjectMocks
    private ProfileDomService profileDomService = new ProfileDomServiceImpl();

    @Mock
    private TeamRepo teamRepo;
    @Mock
    private ProfileFactory profileFactory;
    @Mock
    private UserRepo userRepo;

    public void given2TeamPos() {
        List<TeamPo> teamPoList = new ArrayList<>();
        TeamPo teamPo1 = new TeamPo();
        teamPo1.setTeamId("T1");
        teamPo1.setName("Team 1");
        teamPoList.add(teamPo1);

        TeamPo teamPo2 = new TeamPo();
        teamPo2.setTeamId("T2");
        teamPo2.setName("Team 2");
        teamPoList.add(teamPo2);
        when(teamRepo.findAll()).thenReturn(teamPoList);

        Team team1 = new Team();
        team1.setTeamId("T1");
        team1.setName("Team 1");
        Team team2 = new Team();
        team2.setTeamId("T2");
        team2.setName("Team 2");
        when(profileFactory.getTeam(teamPo1)).thenReturn(team1);
        when(profileFactory.getTeam(teamPo2)).thenReturn(team2);
    }

    @Test
    @DisplayName("return 2 teams when given totally 2 in repository")
    void findAllTeams() {
        given2TeamPos();
        List<Team> allTeams = profileDomService.findAllTeams();
        assertAll(() -> assertThat(allTeams.size()).isEqualTo(2),
                () -> assertThat(allTeams.get(0).getTeamId()).isEqualTo("T1"),
                () -> assertThat(allTeams.get(1).getName()).isEqualTo("Team 2")
        );
    }

    @Test
    void findInterns() {
        profileDomService.findInterns(null, null);
        verify(userRepo, times(1)).findActiveInterns(any(), any());
        verify(profileFactory, never()).getIntern(any());


        reset(userRepo);
        reset(profileFactory);

        UserPo userPo = new UserPo();
        when(userRepo.findActiveInterns(any(), any())).thenReturn(Lists.newArrayList(userPo));
        Intern intern = new Intern();
        when(profileFactory.getIntern(any())).thenReturn(intern);

        List<Intern> interns = profileDomService.findInterns(new Date(), new Date());
        assertEquals(1, interns.size());
        assertEquals(intern, interns.get(0));
        verify(userRepo, times(1)).findActiveInterns(any(Date.class), any(Date.class));
        verify(profileFactory, times(1)).getIntern(userPo);
    }

    @Test
    void findTeamByUserId() {
        UserPo intern = new UserPo();
        intern.setRole(Role.INTERN.getFullName());

        when(userRepo.findById(any())).thenReturn(Optional.of(intern));
        profileDomService.findTeamByUserId("test");

        intern.setTeamId("teamId");
        when(teamRepo.findById(any())).thenReturn(Optional.of(new TeamPo()));
        when(profileFactory.getTeam(any())).thenReturn(new Team());
        Team team = profileDomService.findTeamByUserId("test");
        assertNotNull(team);

        UserPo leader = new UserPo();
        leader.setDomainId("leaderId");
        leader.setRole(Role.TEAM_LEADER.getFullName());
        when(userRepo.findById(any())).thenReturn(Optional.of(leader));
        when(teamRepo.findFirstByTeamLeaderId("leaderId")).thenReturn(Optional.of(new TeamPo()));
        when(profileFactory.getTeam(any())).thenReturn(new Team());
        Team team2 = profileDomService.findTeamByUserId("leaderId");
        assertNotNull(team2);
    }

    @Test
    void findUserByUserTypeAndRoleEmptyTest() {
        when(userRepo.findByRoleContains(Role.TEAM_LEADER.getFullName())).thenReturn(Lists.newArrayList());
        List<User> users = profileDomService.findUserByRole(Role.TEAM_LEADER);
        assertTrue(CollectionUtils.isEmpty(users));
    }

    @Test
    void findUserByUserTypeAndRoleTest() {
        UserPo userPo = new UserPo();
        userPo.setDomainId("leader");
        userPo.setRole(Role.TEAM_LEADER.getFullName());
        Employee employee = new Employee();
        employee.setRoles(Collections.singletonList(Role.TEAM_LEADER));
        employee.setDomainId("leader");
        when(userRepo.findByRoleContains(Role.TEAM_LEADER.getFullName())).thenReturn(Collections.singletonList(userPo));
        when(profileFactory.getUser(userPo)).thenReturn(employee);
        List<User> users = profileDomService.findUserByRole(Role.TEAM_LEADER);
        User result = users.get(0);
        assertAll(() -> result.getDomainId().equals("leader"),
                () -> result.getRoles().get(0).getFullName().equals(Role.TEAM_LEADER.getFullName()));
    }
}