package com.oocl.workshop.intern.domain.report.service.impl;

import com.oocl.workshop.intern.domain.report.repostitory.facade.MonthlySettlementDayRuleRepo;
import com.oocl.workshop.intern.domain.report.service.MonthlySettlementDayRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MonthlySettlementDayRuleServiceImpl implements MonthlySettlementDayRuleService {
    @Autowired
    private MonthlySettlementDayRuleRepo ruleRepo;

    @Override
    public int getMonthlySettlementDay() {
        return ruleRepo.getMonthlySettlementDay();
    }
}
