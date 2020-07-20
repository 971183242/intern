package com.oocl.workshop.intern.domain.profile.service;

import com.oocl.workshop.intern.domain.profile.entity.Intern;
import com.oocl.workshop.intern.domain.profile.entity.Team;
import com.oocl.workshop.intern.domain.profile.entity.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ProfileDomService {
    List<Team> findAllTeams();

    User createUser(User user);

    User updateUser(User user);

    Optional<User> findUserByDomainId(String domainId);

    List<Intern> findTeamInterns(String teamId, Date from, Date to);

    List<Intern> findInterns(Date from, Date to);

    boolean deleteUser(String domainId);
}
