package com.oocl.workshop.intern.domain.profile.service.impl;

import com.oocl.workshop.intern.domain.profile.entity.Intern;
import com.oocl.workshop.intern.domain.profile.repository.facade.UserRepo;
import com.oocl.workshop.intern.domain.profile.repository.po.BaseUserPo;
import com.oocl.workshop.intern.domain.profile.service.ProfileDomainService;
import com.oocl.workshop.intern.domain.profile.service.ProfileFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileDomainServiceImpl implements ProfileDomainService {

    @Autowired
    ProfileFactory profileFactory;

    @Autowired
    UserRepo userRepo;

    @Override
    public Intern createIntern(Intern intern) {
        BaseUserPo internPo = profileFactory.createPo(intern);
        userRepo.save(internPo);
        return intern;
    }
}
