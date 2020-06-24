package com.oocl.workshop.intern.domain.attendance.repostitory.facade;

import com.oocl.workshop.intern.domain.report.repostitory.facade.MonthlySettlementDayRuleRepo;
import com.oocl.workshop.intern.domain.report.repostitory.po.MonthlySettlementDayRule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
class MonthlySettlementDayRuleRepoTest {

    @Autowired
    MonthlySettlementDayRuleRepo ruleRepo;

    @Test
    void getRule() {
        MonthlySettlementDayRule rule = new MonthlySettlementDayRule();
        rule.setDay(20);
        ruleRepo.save(rule);

        assertEquals(20, ruleRepo.getMonthlySettlementDay());
    }

    @Test
    void getNone() {
        assertEquals(25, ruleRepo.getMonthlySettlementDay());
    }
}