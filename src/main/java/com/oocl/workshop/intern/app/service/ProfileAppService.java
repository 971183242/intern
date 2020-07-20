package com.oocl.workshop.intern.app.service;

import com.oocl.workshop.intern.domain.profile.entity.Intern;
import com.oocl.workshop.intern.domain.profile.entity.Team;
import com.oocl.workshop.intern.domain.profile.entity.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ProfileAppService {
    List<Team> findAllTeams();

    User createUser(User user);

    User updateUser(User user);

    Optional<User> findUserByDomainId(String domainId);

    List<Intern> findTeamInterns(String teamId, Date date);

    List<String> getRoles();

    List<Intern> getInterns(Date date);

    boolean deleteUser(String domainId);
}
