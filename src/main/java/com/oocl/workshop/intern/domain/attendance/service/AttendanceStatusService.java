package com.oocl.workshop.intern.domain.attendance.service;

import com.oocl.workshop.intern.domain.attendance.entity.DailyAttendance;

import java.util.Date;
import java.util.List;

public interface AttendanceStatusService {

    /**
     * Usage: find specific Attendances, which date is between dateFrom and dateTo
     *
     * internId: 用来指定需要找到哪一个intern的签到记录
     *
     * dateFrom & dateTo: 考虑到业务上常见的Case，一次查询的签到记录往往是跨月的
     * 所以这里没有查询指定月份签到记录的Api,而是用dateFrom和dateTo来代替
     * 例如Leader在查看指定intern的签到记录时，关注的可能是上月25日到本月25日的签到记录情况
     * */
    List<DailyAttendance> findAttendancesByInternIdAndDate(Long internId, Date dateFrom, Date dateTo);

    /**
     * Usage: create an Attendance Log or update it
     *
     * For batch operation，like batch approve, use it with loop*/
    DailyAttendance createAttendance(DailyAttendance attendance);

    DailyAttendance updateAttendance(DailyAttendance attendance);



}
