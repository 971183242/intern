package com.demo.workshop.intern.domain.report.repostitory.facade;

import com.demo.workshop.intern.domain.report.repostitory.po.MonthlySettlementDayRule;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MonthlySettlementDayRuleRepo extends CrudRepository<MonthlySettlementDayRule, Integer> {
    @Query(value = "select ifnull(max(day)," + MonthlySettlementDayRule.DEFAULT_DAY + ") from T_SETTLEMENT_DAY", nativeQuery = true)
    int getMonthlySettlementDay();
}
