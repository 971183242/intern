package com.oocl.workshop.intern.domain.profile.repository.facade;

import com.oocl.workshop.intern.domain.profile.repository.po.TeamPo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Sql("classpath:test-sqls/insert-team.sql")
class TeamRepoTest {

    @Autowired
    private TeamRepo teamRepo;

    @Test
    void findByTeamLeaderId() {
        Optional<TeamPo> team = teamRepo.findFirstByTeamLeaderId("DONGJA3");
        assertTrue(team.isPresent());
        assertEquals("FWK", team.get().getName());
        assertEquals("FWK", team.get().getTeamId());
    }
}