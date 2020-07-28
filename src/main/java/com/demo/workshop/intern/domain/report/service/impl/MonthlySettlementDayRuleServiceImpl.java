package com.demo.workshop.intern.domain.report.service.impl;

import com.demo.workshop.intern.domain.report.service.MonthlySettlementDayRuleService;
import com.google.common.collect.ImmutableList;
import com.demo.workshop.intern.domain.report.repostitory.facade.MonthlySettlementDayRuleRepo;
import com.demo.workshop.intern.support.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MonthlySettlementDayRuleServiceImpl implements MonthlySettlementDayRuleService {
    static Logger logger = LoggerFactory.getLogger(MonthlySettlementDayRuleServiceImpl.class);

    private MonthlySettlementDayRuleRepo ruleRepo;

    @Autowired
    public void setRuleRepo(MonthlySettlementDayRuleRepo ruleRepo) {
        this.ruleRepo = ruleRepo;
    }

    @Override
    public List<Date> getMonthlySettlementDateWindow(Date baseDate) {
        logger.info("getMonthlySettlementDateWindow. baseDate:" + baseDate);
        int monthlySettlementDay = ruleRepo.getMonthlySettlementDay();
        Date startDate = DateUtil.getMonthSettlementStartDate(monthlySettlementDay, baseDate);
        Date endDate = DateUtil.getMonthSettlementEndDate(monthlySettlementDay, baseDate);
        logger.info("getMonthlySettlementDateWindow. startDate:" + baseDate + ", endDate:" + endDate);
        return ImmutableList.of(startDate, endDate);
    }
}
