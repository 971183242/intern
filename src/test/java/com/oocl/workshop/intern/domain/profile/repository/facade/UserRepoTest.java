package com.oocl.workshop.intern.domain.profile.repository.facade;

import com.oocl.workshop.intern.domain.profile.repository.po.InternPo;
import com.oocl.workshop.intern.domain.profile.repository.po.UserPo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class UserRepoTest {

    @Autowired
    UserRepo userRepo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testFindAll() {
        UserPo userPo = new UserPo();
        userPo.setName("domain");
        entityManager.getEntityManager().persist(userPo);
        List<UserPo> all = userRepo.findAll();
        assert all.size() > 0;
    }

    @Test
    public void testSaveIntern() {
        InternPo internPo = new InternPo();
        internPo.setName("sd");
        internPo.getPeriod().setDateFrom(new Date());
        entityManager.getEntityManager().persist(internPo);
    }
}
