package com.oocl.workshop.intern.domain.profile.repository.facade;

import com.oocl.workshop.intern.domain.profile.repository.po.TeamLeaderPo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamLeaderRepo extends JpaRepository<TeamLeaderPo, Long> {
}
