package com.oocl.workshop.intern.domain.attendance.repostitory.facade;

import com.oocl.workshop.intern.domain.attendance.repostitory.po.MonthlySettlementDayRule;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import static com.oocl.workshop.intern.domain.attendance.repostitory.po.MonthlySettlementDayRule.DEFAULT_DAY;

public interface MonthlySettlementDayRuleRepo extends CrudRepository<MonthlySettlementDayRule, Integer> {
    @Query(value = "select ifnull(max(day)," + DEFAULT_DAY + ") from T_SETTLEMENT_DAY", nativeQuery = true)
    int getMonthlySettlementDay();
}
