package com.oocl.workshop.intern.domain.profile.service.impl;

import com.oocl.workshop.intern.domain.profile.entity.Intern;
import com.oocl.workshop.intern.domain.profile.entity.User;
import com.oocl.workshop.intern.domain.profile.entity.UserType;
import com.oocl.workshop.intern.domain.profile.repository.facade.UserRepo;
import com.oocl.workshop.intern.domain.profile.repository.po.UserPo;
import com.oocl.workshop.intern.domain.profile.service.ProfileFactory;
import com.oocl.workshop.intern.domain.profile.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
        UserPo userPo = profileFactory.createPo(user);
        userPo = userRepo.save(userPo);
        return profileFactory.getUser(userPo);
    }

    @Override
    public User updateUser(User user) {
        UserPo userPo = profileFactory.createPo(user);
        userPo = userRepo.save(userPo);
        return profileFactory.getUser(userPo);
    }

    @Override
    public Optional<User> findUserByDomainId(String domainId) {
        User user = userRepo.findById(domainId)
                .map(profileFactory::getUser)
                .orElse(null);
        return Optional.ofNullable(user);
    }

    @Override
    public List<Intern> findInternsByTeamId(String teamId) {
        List<UserPo> internPoList = userRepo.findByUserTypeAndTeamId(UserType.INTERN, teamId);
        List<Intern> internList = internPoList.stream()
                .map(profileFactory::getIntern)
                .collect(Collectors.toList());
        return internList;
    }

}
