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
        team0.setName("项目组甲");
        team0.setTeamLeaderId(1L);
        teamRepo.save(team0);

        TeamPo team1 = new TeamPo();
        team1.setName("项目组乙");
        team1.setTeamLeaderId(2L);
        teamRepo.save(team1);
    }

    @Test
    void findByName() {
        Optional<TeamPo> team = teamRepo.findByName("项目组甲");
        assertTrue(team.isPresent());
    }

    @Test
    void findByTeamLeaderId() {
        Optional<TeamPo> team = teamRepo.findByTeamLeaderId(2L);
        assertTrue(team.isPresent());
        assertEquals("项目组乙", team.get().getName());
    }
}