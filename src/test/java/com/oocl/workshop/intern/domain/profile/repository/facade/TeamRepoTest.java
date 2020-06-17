package com.oocl.workshop.intern.domain.profile.repository.facade;

import com.oocl.workshop.intern.domain.profile.repository.po.TeamPo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TeamRepoTest {

    @Autowired
    private TeamRepo teamRepo;

    @BeforeAll
    public void before() {
        TeamPo team0 = new TeamPo();
        team0.setTeamId("team_00");
        team0.setName("项目组甲");
        team0.setTeamLeaderId("test_user_01");
        teamRepo.save(team0);

        TeamPo team1 = new TeamPo();
        team1.setTeamId("team_01");
        team1.setName("项目组乙");
        team1.setTeamLeaderId("test_user_02");
        teamRepo.save(team1);

        TeamPo team2 = new TeamPo();
        team2.setTeamId("team_02");
        team2.setName("项目组丙");
        team2.setTeamLeaderId("test_user_02");
        teamRepo.save(team2);
    }

    @Test
    void findByTeamLeaderId() {
        Optional<TeamPo> team = teamRepo.findFirstByTeamLeaderId("test_user_02");
        assertTrue(team.isPresent());
        assertEquals("项目组乙", team.get().getName());
    }
}