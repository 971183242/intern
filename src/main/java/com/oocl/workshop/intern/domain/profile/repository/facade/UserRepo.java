package com.oocl.workshop.intern.domain.profile.repository.facade;

import com.oocl.workshop.intern.domain.profile.entity.UserType;
import com.oocl.workshop.intern.domain.profile.repository.po.UserPo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepo extends JpaRepository<UserPo, Long> {
    UserPo findUserByDomainId(String domainId);

    List<UserPo> findByUserType(UserType userType);
}
