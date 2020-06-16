package com.oocl.workshop.intern.domain.profile.service;

import com.oocl.workshop.intern.domain.profile.entity.Intern;
import com.oocl.workshop.intern.domain.profile.entity.User;
import com.oocl.workshop.intern.domain.profile.repository.po.BaseUserPo;
import org.springframework.stereotype.Service;

@Service
public class ProfileFactory {
    public BaseUserPo createPo(User user){
        if(user instanceof  Intern){
            return  createInternPo((Intern)user);
        }
        return null;
    }

    private BaseUserPo createInternPo(Intern intern){
        return null;
    }

    public User getUser(BaseUserPo po){
        return null;
    }
}
