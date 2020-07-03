package com.oocl.workshop.intern.domain.report.service;

import java.util.Date;
import java.util.List;

public interface MonthlySettlementDayRuleService {

    List<Date> getMonthlySettlementDateWindow(Date baseDate);
}
