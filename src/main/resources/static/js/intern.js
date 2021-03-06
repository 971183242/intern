import {Alert} from "./alert.js";

/**
 * 点击日期按钮触发
 */
$(document).on("click", ".calendar-cell-active", function (e) {
    e.stopPropagation();
    let tipStr = '';
    let status = $(this).children(":last").attr("status");
    let that = $(this);
    if (status === undefined) {
        let date = new Date(that.attr('relTime'));
        if (date > new Date()) {
            Alert.rejectFutureSign();
            return;
        }
        let attendanceId = createAttendance(date).attendanceId;
        that.attr('attendanceId', attendanceId)
        tipStr = checkInStr;
        Alert.applySuccess();
    } else if (status === '1') {
        tipStr = '<div class="calendar-empty"><div> ';
        removeAttendance(that.attr('attendanceId'));
        Alert.cancelSuccess();
        that.find(":last-child").css("display", "none");
    } else if (status === '2'){
        let needConfirmedAttendanceList = [];
        let attendance = {};
        attendance.attendanceId = that.attr('attendanceId');
        attendance.attendanceStatus = 'CheckedIn';
        needConfirmedAttendanceList.push(attendance);
        confirmAttendances(needConfirmedAttendanceList);
        tipStr = checkInStr;
        Alert.reApplySuccess();
        that.find(":last-child").css("display", "none");
    }
    that.append(tipStr);
});

/**
 *  提交签到请求
 */
let createAttendance = function(date) {
    let newAttendance = {};
    let attendance = {};
    attendance.internId = currentIntern;
    attendance.workDay = dateFormat("YYYY-mm-dd", date);
    $.ajax({
        url: '/attendance/checkIn',
        type: 'post',
        dataType: 'json',
        data: JSON.stringify(attendance),
        contentType: 'application/json;charset=UTF-8',
        async: false,
        success: function (data) {
            if (data.code === 1) {
                newAttendance =  data.data;
            } else {
                Alert.error(data.msg);
            }
        },
        error: function (XMLHttpRequest) {
            Alert.error(XMLHttpRequest.status);
        }
    });
    return newAttendance;
};

/**
 *  移除签到请求
 */
let removeAttendance = function(attendanceId) {
    let attendance = {};
    attendance.attendanceId = attendanceId;
    $.ajax({
        url: '/attendance/cancelCheckIn',
        type: 'post',
        dataType: 'json',
        data: JSON.stringify(attendance),
        contentType: 'application/json;charset=UTF-8',
        async: false,
        success: function (data) {
            if (data.code === 1) {
            } else {
                Alert.error(data.msg);
            }
        },
        error: function (XMLHttpRequest) {
            Alert.error(XMLHttpRequest.status);
        }
    })
};

/**
 *  重新提交签到
 */
let confirmAttendances = function(needConfirmedAttendanceList) {
    $.ajax({
        url: '/attendance/confirm',
        type: 'post',
        dataType: 'json',
        data: JSON.stringify(needConfirmedAttendanceList),
        contentType: 'application/json;charset=UTF-8',
        async: false,
        success: function (data) {
            if (data.code === 1) {
            } else {
                Alert.error(data.msg);
            }
        },
        error: function (XMLHttpRequest) {
            Alert.error(XMLHttpRequest.status);
        }
    })
};

/**
 * 初始化日历
 */
SyntaxHighlighter.all();
new SYSUI({
    Module: '#pageModule',
    Method: [{'module': '#calendar',event: function(set, obj) {
            var m=set.$(set.par.Module);
            m.style.height=set.height()+'px';
            m.style.width=set.width()+'px';
            var scrollDiv=set.$("#pagescroll");
            set.scrollbar(set,m,scrollDiv);//滚动条调用方法
            $("#calendar").calendar({
                data: [], //记录数据
                holiday: [], //假日规划时间
                work:[],//上班时间
                mode: "month",
                width:'80%',
                monthClick: function(e, nextMonth, opts, me) {
                    //开始月份第一天
                    var start = me._cloneDate(opts.newDate);
                    start.setDate(1);
                    // 获取当前月的最后一天
                    me._refreshCalendar(opts.newDate, null);
                }
            });
        }
    }]
});
//初始化
let nowDate = new Date();
nowDateStr = dateFormat("YYYY-mm-dd", nowDate);
currentIntern = $('#domainId').val();
//获得签到信息
getAttendances(currentIntern, nowDateStr);
if (new Date().getDate() > 20) {
    $('.next-month-item').click();
}
//初始化签到信息
initAttendance();

