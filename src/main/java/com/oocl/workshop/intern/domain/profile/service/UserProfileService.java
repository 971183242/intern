package com.oocl.workshop.intern.domain.profile.service;

import com.oocl.workshop.intern.domain.profile.entity.Intern;
import com.oocl.workshop.intern.domain.profile.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserProfileService {
    User createUser(User user);

    User updateUser(User user);

    Optional<User> findUserByDomainId(String domainId);

    List<Intern> findInternsByTeamId(String teamId);

}
