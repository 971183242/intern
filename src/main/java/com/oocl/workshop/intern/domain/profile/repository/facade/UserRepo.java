package com.oocl.workshop.intern.domain.profile.repository.facade;

import com.oocl.workshop.intern.domain.profile.entity.UserType;
import com.oocl.workshop.intern.domain.profile.repository.po.TeamPo;
import com.oocl.workshop.intern.domain.profile.repository.po.UserPo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepo extends JpaRepository<UserPo, Long> {
    UserPo findUserByDomainId(String domainId);

    List<UserPo> findByUserType(UserType userType);

    Page<UserPo> findByUserType(UserType userType, Pageable pageable);

    Page<UserPo> findByUserTypeAndTeam(UserType userType, TeamPo teamPo, Pageable pageable);

    @Query("select u from UserPo u where u.internPeriod.dateTo < sysdate" +
            " and u.userType = 'Intern' and u.team = ?1")
    Page<UserPo> findActiveInternByTeam(TeamPo teamPo, Pageable pageable);
}
