package com.demo.workshop.intern.app.service;

import com.demo.workshop.intern.domain.profile.entity.Intern;
import com.demo.workshop.intern.domain.profile.entity.Team;
import com.demo.workshop.intern.domain.profile.entity.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ProfileAppService {
    List<Team> findAllTeams();

    User createUser(User user);

    User updateUser(User user);

    Optional<User> findUserByDomainId(String domainId);

    List<Intern> getInterns(Date date);

    Team findTeam(String userDomainId);
}
