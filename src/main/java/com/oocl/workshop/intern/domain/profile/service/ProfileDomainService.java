package com.oocl.workshop.intern.domain.profile.service;

import com.oocl.workshop.intern.domain.profile.entity.Intern;
import com.oocl.workshop.intern.domain.profile.entity.TeamLeader;
import com.oocl.workshop.intern.domain.profile.entity.User;
import com.oocl.workshop.intern.infrastructure.common.entity.PageInfo;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ProfileDomainService {
    Intern createIntern(Intern intern);

    Intern deleteIntern(Intern intern);

    Intern updateIntern(Intern intern);

    Optional<User> findUserByDomainId(String domainId);

    Optional<TeamLeader> findTeamLeaderByDomainId(String domainId);

    Page<Intern> findInternByTeamId(Long teamId, PageInfo pageInfo);

    Page<Intern> findActiveInternByTeamId(Long teamId, PageInfo pageInfo);

    Page<Intern> findIntern(PageInfo pageInfo);
}
