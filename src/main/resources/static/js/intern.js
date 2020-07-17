import {Alert} from "./alert.js";


$(document).on("click", ".calendar-cell-active", function (e) {
    e.stopPropagation();
    let tipStr = '';
    let status = $(this).children(":last").attr("status");
    let that = $(this);
    if (status === undefined) {
        let date = new Date(that.attr('relTime'));
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
            console.log(data);
            if (data.code === 1) {
                newAttendance =  data.data;
            } else {

            }
        },
        error: function (XMLHttpRequest) {
        }
    });
    return newAttendance;
};
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
            console.log(data);
            if (data.code === 1) {
            } else {

            }
        },
        error: function (XMLHttpRequest) {
        }
    })
};

let confirmAttendances = function(needConfirmedAttendanceList) {
    $.ajax({
        url: '/attendance/confirm',
        type: 'post',
        dataType: 'json',
        data: JSON.stringify(needConfirmedAttendanceList),
        contentType: 'application/json;charset=UTF-8',
        async: false,
        success: function (data) {
            console.log(data);
            if (data.code === 1) {
            } else {

            }
        },
        error: function (XMLHttpRequest) {
        }
    })
};


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
let nowDate = new Date();
nowDateStr = dateFormat("YYYY-mm-dd", nowDate);
currentIntern = 'User1';
getAttendances(currentIntern, nowDateStr);
initAttendance();

