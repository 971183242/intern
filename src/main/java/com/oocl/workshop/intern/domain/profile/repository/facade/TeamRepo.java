package com.oocl.workshop.intern.domain.profile.repository.facade;

import com.oocl.workshop.intern.domain.profile.repository.po.TeamPo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepo extends JpaRepository<TeamPo, Long> {
    Optional<TeamPo> findByName(String name);

    Optional<TeamPo> findByTeamLeaderId(Long teamLeaderId);
}
