package com.oocl.workshop.intern.domain.profile.service.impl;

import com.oocl.workshop.intern.domain.profile.entity.Intern;
import com.oocl.workshop.intern.domain.profile.entity.User;
import com.oocl.workshop.intern.domain.profile.entity.UserType;
import com.oocl.workshop.intern.domain.profile.repository.facade.TeamRepo;
import com.oocl.workshop.intern.domain.profile.repository.facade.UserRepo;
import com.oocl.workshop.intern.domain.profile.repository.po.UserPo;
import com.oocl.workshop.intern.domain.profile.service.ProfileFactory;
import com.oocl.workshop.intern.domain.profile.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    private ProfileFactory profileFactory;

    @Autowired
    private UserRepo userRepo;


    @Override
    public User createUser(User user) {
        userRepo.save(profileFactory.createPo(user));
        return user;
    }

    @Override
    public User updateUser(User user) {
        userRepo.save(profileFactory.createPo(user));
        return user;
    }

    @Override
    public Optional<User> findUserByDomainId(String domainId) {
        Objects.requireNonNull(domainId, "DomainId should not be empty");
        User user = userRepo.findById(domainId)
                .map(profileFactory::getUser)
                .orElse(null);
        return Optional.ofNullable(user);
    }

    @Override
    public List<Intern> findInternsByTeamId(String teamId) {
        List<UserPo> internPoList = userRepo.findByUserTypeAndTeamId(UserType.Intern, teamId);
        List<Intern> internList = internPoList.stream()
                .map(profileFactory::getIntern)
                .collect(Collectors.toList());
        return internList;
    }

}
