package com.demo.workshop.intern.domain.report.service.impl;

import com.demo.workshop.intern.domain.report.repostitory.facade.MonthlySettlementDayRuleRepo;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MonthlySettlementDayRuleServiceImplTest {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Test
    void checkDateWindow20() throws ParseException {
        checkDateWindow(20, "2020-07-03", "2020-06-21", "2020-07-20");
        checkDateWindow(20, "2020-07-20", "2020-06-21", "2020-07-20");
        checkDateWindow(20, "2020-07-21", "2020-07-21", "2020-08-20");
    }

    @Test
    void checkDateWindow31() throws ParseException {
        checkDateWindow(31, "2020-07-21", "2020-07-01", "2020-07-31");
        checkDateWindow(31, "2020-07-31", "2020-07-01", "2020-07-31");
        checkDateWindow(31, "2020-08-01", "2020-08-01", "2020-08-31");
        checkDateWindow(31, "2020-06-30", "2020-06-01", "2020-06-30");
    }

    @Test
    void checkDateWindow30() throws ParseException {
        checkDateWindow(30, "2020-07-21", "2020-07-01", "2020-07-30");
        checkDateWindow(30, "2020-07-30", "2020-07-01", "2020-07-30");
        checkDateWindow(30, "2020-07-31", "2020-07-31", "2020-08-30");
        checkDateWindow(30, "2020-02-29", "2020-01-31", "2020-02-29");
        checkDateWindow(30, "2020-03-01", "2020-03-01", "2020-03-30");
    }

    @Test
    void checkDateWindow28() throws ParseException {
        checkDateWindow(28, "2019-02-21", "2019-01-29", "2019-02-28");
        checkDateWindow(28, "2019-02-28", "2019-01-29", "2019-02-28");
        checkDateWindow(28, "2019-01-31", "2019-01-29", "2019-02-28");
        checkDateWindow(28, "2019-01-28", "2018-12-29", "2019-01-28");
        checkDateWindow(28, "2019-03-01", "2019-03-01", "2019-03-28");
    }

    private void checkDateWindow(int settlementDay, String baseDate, String startDate, String endDate) throws ParseException {
        MonthlySettlementDayRuleServiceImpl service = new MonthlySettlementDayRuleServiceImpl();
        MonthlySettlementDayRuleRepo ruleRepo = mock(MonthlySettlementDayRuleRepo.class);
        when(ruleRepo.getMonthlySettlementDay()).thenReturn(settlementDay);
        service.setRuleRepo(ruleRepo);
        List<Date> dateWindow = service.getMonthlySettlementDateWindow(sdf.parse(baseDate));
        assertEquals(startDate, sdf.format(dateWindow.get(0)));
        assertEquals(endDate, sdf.format(dateWindow.get(1)));
    }
}