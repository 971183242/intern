package com.oocl.workshop.intern.support.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;

public class DateUtil {

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd hh:mm:ss";

    public static Date parseDate(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        return sdf.parse(dateStr);
    }

    public static Date parseDateTime(String dateTimeStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT);
        return sdf.parse(dateTimeStr);
    }

    public static String formatDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        return sdf.format(date);
    }

    public static String formatDateTime(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT);
        return sdf.format(date);
    }

    public static Date getMonthSettlementStartDate(int monthlySettlementDay, Date baseDate) {
        LocalDate date = baseDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate startDate = date;

        if (date.getDayOfMonth() <= monthlySettlementDay) {
            startDate = startDate.minusMonths(1);
        }
        int monthLength = getLengthOfYearMonth(startDate.getYear(), startDate.getMonthValue());
        startDate = startDate.withDayOfMonth(monthlySettlementDay > monthLength ? monthLength : monthlySettlementDay).plusDays(1);

        return Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static Date getMonthSettlementEndDate(int monthlySettlementDay, Date baseDate) {
        LocalDate date = baseDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDate = LocalDate.from(date);

        if (date.getDayOfMonth() > monthlySettlementDay) {
            endDate = endDate.plusMonths(1);
        }
        int monthLength = getLengthOfYearMonth(date.getYear(), date.getMonthValue());
        endDate = endDate.withDayOfMonth(monthlySettlementDay > monthLength ? monthLength : monthlySettlementDay);

        return Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static int getLengthOfYearMonth(int year, int month) {
        return YearMonth.of(year, month).lengthOfMonth();
    }
}
