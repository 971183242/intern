package com.oocl.workshop.intern.domain.report.service;

import java.util.Date;
import java.util.List;

public interface MonthlySettlementDayRuleService {
    int getMonthlySettlementDay();

    List<Date> getMonthlySettlementDateWindow(Date baseDate);
}
