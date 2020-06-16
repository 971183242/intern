package com.oocl.workshop.intern.domain.profile.repository.facade;

import com.oocl.workshop.intern.domain.profile.repository.po.BaseUserPo;
import com.oocl.workshop.intern.domain.profile.repository.po.InternPo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<BaseUserPo, Long> {
    BaseUserPo findUserByDomainId(String domainId);

    InternPo findInternByDomainId(String domainId);
}
