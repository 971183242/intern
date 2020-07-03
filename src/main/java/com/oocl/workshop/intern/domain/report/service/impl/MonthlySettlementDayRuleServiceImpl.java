package com.oocl.workshop.intern.domain.report.service.impl;

import com.google.common.collect.ImmutableList;
import com.oocl.workshop.intern.domain.report.repostitory.facade.MonthlySettlementDayRuleRepo;
import com.oocl.workshop.intern.domain.report.service.MonthlySettlementDayRuleService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.time.DateUtils.addDays;
import static org.apache.commons.lang3.time.DateUtils.addMonths;
import static org.apache.commons.lang3.time.DateUtils.setDays;

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
        Date startDate = getStartDate(baseDate, monthlySettlementDay);
        if (DateUtils.getFragmentInDays(baseDate, Calendar.DAY_OF_MONTH) < monthlySettlementDay) {
            startDate = addMonths(startDate, -1);
        }
        return ImmutableList.of(startDate, addDays(addMonths(startDate, 1), -1));
    }

    private Date getStartDate(Date baseDate, int monthlySettlementDay) {
        Date lastDateOfMonth = addDays(addMonths(setDays(baseDate, 1), 1), -1);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(lastDateOfMonth);
        int lastDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        if (lastDayOfMonth < monthlySettlementDay) {
            return addDays(lastDateOfMonth, 1);
        }
        return addDays(setDays(baseDate, monthlySettlementDay), 1);
    }
}
