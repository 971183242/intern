package com.oocl.workshop.intern.domain.profile.repository.facade;

import com.oocl.workshop.intern.domain.profile.entity.UserType;
import com.oocl.workshop.intern.domain.profile.repository.po.UserPo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepo extends JpaRepository<UserPo, String> {
    List<UserPo> findByUserType(UserType userType);

    List<UserPo> findByUserTypeAndTeamId(UserType userType, String teamId);

    Page<UserPo> findByUserType(UserType userType, Pageable pageable);
}
