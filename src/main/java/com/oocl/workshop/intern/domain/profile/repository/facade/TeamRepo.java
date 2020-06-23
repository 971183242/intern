package com.oocl.workshop.intern.domain.profile.repository.facade;

import com.oocl.workshop.intern.domain.profile.repository.po.TeamPo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepo extends JpaRepository<TeamPo, String> {
    Optional<TeamPo> findFirstByTeamLeaderId(String teamLeaderId);
}
