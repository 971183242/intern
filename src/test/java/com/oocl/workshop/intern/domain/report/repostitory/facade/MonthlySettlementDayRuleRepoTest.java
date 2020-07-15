package com.oocl.workshop.intern.domain.report.repostitory.facade;

import com.oocl.workshop.intern.domain.report.repostitory.po.MonthlySettlementDayRule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
class MonthlySettlementDayRuleRepoTest {

    @Autowired
    MonthlySettlementDayRuleRepo ruleRepo;

    @Test
    void getRule() {
        MonthlySettlementDayRule rule = new MonthlySettlementDayRule();
        rule.setDay(21);
        ruleRepo.save(rule);

        assertEquals(21, ruleRepo.getMonthlySettlementDay());
    }

    @Test
    void getNone() {
        assertEquals(20, ruleRepo.getMonthlySettlementDay());
    }
}