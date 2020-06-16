package com.oocl.workshop.intern.domain.profile.repository.facade;

import com.oocl.workshop.intern.domain.profile.repository.po.BaseUserPo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<BaseUserPo, String> {
    BaseUserPo findByUserId(long userId);

    BaseUserPo findUserByDomainId(String domainId);

}
