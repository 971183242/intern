import {Alert} from "./alert.js";

let internList = [];

/**
 * 点击日期按钮触发
 */
$(document).on("click", ".calendar-cell-active", function (e) {
    e.stopPropagation();
    let chooseFlag = $(this).attr("choose");
    if (typeof chooseFlag === 'undefined' || chooseFlag === 'false') {
        $(this).children(":first").css("border", "3px solid #FF6633");
        $(this).attr("choose", "true");
    } else {
        $(this).children(":first").css("border", "3px solid transparent");
        $(this).attr("choose", "false");
    }
});

/**
 * 点击no-active用户触发
 */
$(document).on("click", ".no-active", function () {
    currentIntern = $(this).attr("domainId");
    $('.active').removeClass("active").addClass("no-active");
    $(this).addClass("active").removeClass("no-active");
    removeAttendance();
    getAttendances(currentIntern ,nowDateStr);
    initAttendance();
});

/**
 * 审批
 */
$(document).on('click', '.btn-approve', function () {
    confirmAttendances("Approved")
});
/**
 * 拒批
 */
$(document).on('click', '.btn-reject', function () {
    confirmAttendances("Rejected");
});

/**
 *  审批/拒批 已选择日期数据
 */
let confirmAttendances = function(operation) {
    let choosedAttendanceList = $('*[choose="true"]');
    let needConfirmedAttendanceList = [];
    for (let i = 0; i < choosedAttendanceList.length; i++) {
        let element = choosedAttendanceList[i];
        let attendance = {};
        attendance.attendanceId = element.getAttribute("attendanceId");
        attendance.attendanceStatus = operation;
        let childNodes = element.childNodes;
        element.childNodes[0].style.border = 0;
        element.setAttribute("choose", "false");
        if (attendance.attendanceId !== "") {
            console.log("attendance id: " + attendance.attendanceId);
            needConfirmedAttendanceList.push(attendance);
            element.removeChild(childNodes[childNodes.length-1]);
            let html = element.innerHTML;
            if (operation === 'Approved'){
                element.innerHTML = html + approvedStr;
            }
            if (operation === 'Rejected') {
                element.innerHTML  = html + rejectedStr;
            }

        }
    }
    if(needConfirmedAttendanceList.length > 0) {
        $.ajax({
            url: '/attendance/confirm',
            type: 'post',
            dataType: 'json',
            data: JSON.stringify(needConfirmedAttendanceList),
            contentType: 'application/json;charset=UTF-8',
            async: false,
            success: function (data) {
                if (data) {
                    if (operation === 'Approved') {
                        Alert.approveSuccess();
                    }
                    if (operation === 'Rejected') {
                        Alert.rejectSuccess();
                    }
                } else {
                    Alert.error();
                }
            },
            error: function (XMLHttpRequest) {
                Alert.error(XMLHttpRequest.status);
            }
        })
    }
};

/**
 *  根据teamId获得所有用户
 */
let getTeamUsers = function(teamId, dateStr) {
    $.ajax({
        url: '/attendance/getInterns?teamId=' + teamId + '&date=' + dateStr,
        type: 'get',
        dataType: 'json',
        contentType: 'application/json;charset=UTF-8',
        async: false,
        success: function (data) {
            if (data.code === 1) {
                internList = data.data;
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
 * 初始化用户列表
 */
let initUserList = function() {
    let userListBoxEl = $('.user-list-box');
    userListBoxEl.empty();
    let userListStr = '';
    let activeFlag = 'active';
    for (let i = 0; i < internList.length; i++) {
        let domainId = internList[i].domainId;
        let userName = internList[i].name;
        if (i > 0) {
            activeFlag = 'no-active';
        } else {
            currentIntern = internList[i].domainId;
        }
        userListStr += '<a href="#" class="list-group-item ' + activeFlag + '" domainId="'+ domainId +'">\n' +
            userName +
            '</a>'
    }
    userListBoxEl.append(userListStr);
};

/**
 * 加载日历
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
                width:'70%',
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
let teamId = $('#teamId').val();
let role = $('#role').val();
if (role.indexOf('ROLE_SUPER_ADMIN') > 0) {
    teamId = 'all';
}
getTeamUsers(teamId, nowDateStr);
initUserList();
//获得签到信息
getAttendances(currentIntern, nowDateStr);
if (new Date().getDate() > 20) {
    $('.next-month-item').click();
}
//初始化签到信息
initAttendance();
