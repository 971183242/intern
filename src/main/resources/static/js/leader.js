import {Alert} from "./alert.js";

let internList = [];

$(document).on("click", ".calendar-cell-active", function (e) {
    e.stopPropagation();
    let chooseFlag = $(this).attr("choose");
    if (typeof chooseFlag === 'undefined' || chooseFlag === 'false') {
        $(this).children(":first").css("border", "3px solid #FF6633");
        $(this).attr("choose", "true");
    } else {
        $(this).children(":first").css("border", "0");
        $(this).attr("choose", "false");
    }
});

$(document).on("click", ".no-active", function () {
    currentIntern = $(this).attr("domainId");
    $('.active').removeClass("active").addClass("no-active");
    $(this).addClass("active").removeClass("no-active");
    removeAttendance();
    getAttendances(currentIntern ,nowDateStr);
    initAttendance();
});

$(document).on('click', '.btn-approve', function () {
    confirmAttendances("Approved")
});

$(document).on('click', '.btn-reject', function () {
    confirmAttendances("Rejected");
});


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
                console.log(data);
                if (data) {
                    if (operation === 'Approved') {
                        Alert.approveSuccess();
                    }
                    if (operation === 'Rejected') {
                        Alert.rejectSuccess();
                    }
                } else {

                }
            },
            error: function (XMLHttpRequest) {
            }
        })
    }
};

let getTeamUsers = function(leaderId) {
    $.ajax({
        url: '/mock/profile/users/' + leaderId,
        type: 'get',
        dataType: 'json',
        contentType: 'application/json;charset=UTF-8',
        async: false,
        success: function (data) {
            console.log(data);
            if (data.code === 1) {
                internList = data.data;
            } else {

            }
        },
        error: function (XMLHttpRequest) {
        }
    })
};

let initUserList = function() {
    let userListBoxEl = $('.user-list-box');
    userListBoxEl.empty();
    let userListStr = '';
    let activeFlag = 'active'
    for (let i = 0; i < internList.length; i++) {
        let domainId = internList[i].domainId;
        if (i > 0) {
            activeFlag = 'no-active';
        } else {
            currentIntern = internList[i].domainId;
        }
        userListStr += '<a href="#" class="list-group-item ' + activeFlag + '" domainId="'+ domainId +'">\n' +
            domainId +
            '</a>'
    }
    userListBoxEl.append(userListStr);
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
let nowDate = new Date();
nowDateStr = dateFormat("YYYY-mm-dd", nowDate);
getTeamUsers("22");
initUserList();
getAttendances(currentIntern, nowDateStr);
initAttendance();
