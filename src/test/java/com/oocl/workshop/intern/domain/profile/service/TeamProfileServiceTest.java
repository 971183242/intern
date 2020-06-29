package com.oocl.workshop.intern.domain.profile.service;

import com.oocl.workshop.intern.domain.profile.entity.Team;
import com.oocl.workshop.intern.domain.profile.repository.facade.TeamRepo;
import com.oocl.workshop.intern.domain.profile.repository.po.TeamPo;
import com.oocl.workshop.intern.domain.profile.service.impl.TeamProfileServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamProfileServiceTest {

    @InjectMocks
    private TeamProfileService teamProfileService = new TeamProfileServiceImpl();

    @Mock
    private TeamRepo teamRepo;
    @Mock
    private ProfileFactory profileFactory;

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
        List<Team> allTeams = teamProfileService.findAllTeams();
        assertAll(() -> assertThat(allTeams.size()).isEqualTo(2),
                () -> assertThat(allTeams.get(0).getTeamId()).isEqualTo("T1"),
                () -> assertThat(allTeams.get(1).getName()).isEqualTo("Team 2")
                );
    }
}