package com.oocl.workshop.intern.domain.profile.repository.facade;

import com.oocl.workshop.intern.domain.profile.repository.po.UserPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface UserRepo extends JpaRepository<UserPo, String> {
    List<UserPo> findByRoleContains(String role);

    @Query("select u from UserPo u where u.role = 'ROLE_INTERN' and u.teamId = :teamId and u.internPeriod.dateFrom <= :toDate " +
            "and (u.internPeriod.dateTo >= :fromDate or u.internPeriod.dateTo is null)")
    List<UserPo> findTeamActiveInterns(@Param("teamId") String teamId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    @Query("select u from UserPo u where u.role = 'ROLE_INTERN' and u.internPeriod.dateFrom <= :toDate " +
            "and (u.internPeriod.dateTo >= :fromDate or u.internPeriod.dateTo is null)")
    List<UserPo> findActiveInterns(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
}
