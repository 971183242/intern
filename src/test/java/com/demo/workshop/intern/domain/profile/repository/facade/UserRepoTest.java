package com.demo.workshop.intern.domain.profile.repository.facade;

import com.demo.workshop.intern.domain.profile.entity.Role;
import com.demo.workshop.intern.domain.profile.repository.po.UserPo;
import com.demo.workshop.intern.support.util.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Sql({"classpath:test-sqls/insert-team.sql","classpath:test-sqls/insert-user.sql"})
public class UserRepoTest {
    @Autowired
    UserRepo userRepo;

    @Test
    public void testSuperAdmin() {
        UserPo admin = userRepo.findById("OLIVER").get();
        assertEquals("oliver@demo1.com", admin.getEmail());
        assertEquals("Oliver", admin.getName());
        assertEquals("OLIVER", admin.getDomainId());
        Assertions.assertEquals(Role.SUPER_ADMIN.getFullName(), admin.getRole());
    }


    @Test
    public void findByUserTypeAndRoleContains() {
        List<UserPo> leaders = userRepo.findByRoleContains(Role.TEAM_LEADER.getFullName());
        assertEquals(2, leaders.size());
    }


    @Test
    void findTeamActiveInterns() throws ParseException {
        List<UserPo> users = userRepo.findTeamActiveInterns("TeamA", DateUtil.parseDate("2020-05-01"), DateUtil.parseDate("2020-11-01"));
        assertEquals(2, users.size());
        assertEquals("TOM", users.get(0).getDomainId());
    }

    @Test
    void findActiveInterns() {
        List<UserPo> users = userRepo.findActiveInterns(new Date(120, 4, 21), new Date(120, 5, 20));
        assertEquals(0, users.size());
    }
}
