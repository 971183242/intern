package com.oocl.workshop.intern.domain.report.service.impl;

import com.google.common.collect.ImmutableList;
import com.oocl.workshop.intern.domain.report.repostitory.facade.MonthlySettlementDayRuleRepo;
import com.oocl.workshop.intern.domain.report.service.MonthlySettlementDayRuleService;
import com.oocl.workshop.intern.infrastructure.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MonthlySettlementDayRuleServiceImpl implements MonthlySettlementDayRuleService {
    private MonthlySettlementDayRuleRepo ruleRepo;

    @Autowired
    public void setRuleRepo(MonthlySettlementDayRuleRepo ruleRepo) {
        this.ruleRepo = ruleRepo;
    }

    @Override
    public int getMonthlySettlementDay() {
        return ruleRepo.getMonthlySettlementDay();
    }

    @Override
    public List<Date> getMonthlySettlementDateWindow(Date baseDate) {
        int monthlySettlementDay = ruleRepo.getMonthlySettlementDay();
        Date startDate = DateUtil.getMonthSettlementStartDate(monthlySettlementDay, baseDate);
        Date endDate = DateUtil.getMonthSettlementEndDate(monthlySettlementDay, baseDate);

        return ImmutableList.of(startDate, endDate);
    }
}
