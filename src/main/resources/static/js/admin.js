import {Alert} from "./alert.js";
let teams = [];
let nowDateStr =  dateFormat("YYYY-mm-dd", new Date());
let reg = new RegExp("^[a-zA-Z0-9]+([._\\-]*[a-zA-Z0-9])*@([a-zA-Z0-9]+[-a-zA-Z0-9]*[a-zA-Z0-9]+.){1,63}[a-zA-Z0-9]+$");
let user = {};
let currentUser = [];

/**
 * 加载编辑user modal
 */
$(document).on("click", ".btn-edit", function () {
    $('.edit-user').addClass('update-user').removeClass("create-ser");
    $("#myModalLabel").text("Edit Intern");
    getInternByDomainId($(this).attr("domainId"));
    let domainIdEl = $('#domainIdInput');
    domainIdEl.val(currentUser.domainId);
    domainIdEl.attr('disabled', 'disabled');
    $('#name').val(currentUser.name);
    $('#email').val(currentUser.email);
    $("#fromTime").val(currentUser.internPeriodFromDate);
    $("#toTime").val(currentUser.internPeriodToDate);
    if (currentUser.team !== null) {
        for (let i = 0; i < teams.length; i++) {
            if (teams[i].teamId === currentUser.team.teamId) {
                $('#teamIdSelector').val(i);
                break;
            }
        }
    }
    $("#myModal").modal("show");
});

/**
 * 加载创建user modal
 */
$(document).on("click", ".btn-create", function () {
    $('.edit-user').addClass('create-user').removeClass("update-user");
    //初始化form
    let domainIdEl = $('#domainIdInput');
    domainIdEl.val('');
    domainIdEl.attr('disabled', false);
    $('#name').val('');
    $('#email').val('');
    $("#fromTime").val(nowDateStr);
    $("#myModalLabel").text("Create Intern");
    $("#myModal").modal("show");

});
/**
 * 获得焦点,去除错误提示
 */
$('#domainIdInput, #name, #email').focus(function () {
    $(this).parent().parent().removeClass("has-error");
    $(this).next().text('');
});

/**
 * 开始/结束时间 默认相等
 */
let fromTimeEl = $('#fromTime');
fromTimeEl.blur(function () {
    let fromTime = fromTimeEl.val();
    if (fromTime === '' || fromTime === null) {
        fromTimeEl.val($("#toTime").val());
    }
});
let toTimeEl = $('#toTime');
toTimeEl.blur(function () {
    let toTime = toTimeEl.val();
    if (toTime === '' || toTime === null) {
        toTime.val($("#fromTime").val());
    }
});

/**
 * 提交创建/更新用户请求
 */
$(document).on("click", ".create-user", function () {
    if (generateUser()) {
        operateIntern(user,"/profile/createIntern");
    }
});
$(document).on("click", ".update-user", function () {
    if (generateUser()) {
        operateIntern(user, "/profile/updateIntern");
    }
});

/**
 *  生成user对象
 */
let generateUser = function () {
    let domainIdEl = $('#domainIdInput');
    let domainId = domainIdEl.val();
    if (domainId === '' || domainId === null) {
        domainIdEl.parent().parent().addClass("has-error");
        domainIdEl.next().text("请填写domain id");
        return false;
    }
    domainId = domainId.toUpperCase();

    let nameEl = $('#name');
    let name = nameEl.val();
    if (name === '' || name === null) {
        nameEl.parent().parent().addClass("has-error");
        nameEl.next().text("请填写名字");
        return false;
    }

    let emailEl = $('#email');
    let email = emailEl.val();
    if (email === '' || email === null) {
        emailEl.parent().parent().addClass("has-error");
        emailEl.next().text("请填写邮箱");
        return false;
    }
    if (!reg.test(email)) {
        emailEl.parent().parent().addClass("has-error");
        emailEl.next().text("邮箱格式不正确");
        return false;
    }
    let teamIdIndex = $('#teamIdSelector').val();
    let fromTimeEl = $('#fromTime');
    let fromTime = fromTimeEl.val();
    let toTime = $('#toTime').val();
    user.type = 'EMPLOYEE';
    user.domainId = domainId;
    user.email = email;
    user.name = name;
    user.roles = ['ROLE_INTERN'];
    user.internPeriodFromDate = fromTime;
    user.internPeriodToDate = toTime;
    user.team = teams[teamIdIndex];
    return true;
};

/**
 *  操作用户
 */
let operateIntern = function(user, url) {
    $.ajax({
        url: url,
        type: 'post',
        dataType: 'json',
        data: JSON.stringify(user),
        contentType: 'application/json;charset=UTF-8',
        // async: false,
        success: function () {
            $('#table').bootstrapTable('refresh');
            $("#myModal").modal("hide");
            if (url === '/profile/createIntern') {
                Alert.createSuccess();
            } else {
                Alert.updateSuccess();
            }
        },
        error: function () {
            if (url === '/profile/createIntern') {
                Alert.createFail();
            } else {
                Alert.updateFail();
            }
        }
    });
};

/**
 *  根据Domain Id 获得用户信息
 */
let getInternByDomainId = function(domainId) {
    $.ajax({
        url: '/profile/user/' + domainId,
        type: 'get',
        dataType: 'json',
        contentType: 'application/json;charset=UTF-8',
        async:false,
        success: function (data) {
            currentUser = data;
        },
        error: function () {
            Alert.commonError();
        }
    })
};

/**
 * 初始化表格
 */
$('#table').bootstrapTable({
    url: '/profile/getInterns',
    pagination: false,
    pageSize: 5,
    search: false,
    striped: true,
    sidePagination : 'server',
    pageList : [ 5, 10, 20],
    showSearchButton: true,
    queryParams : function() {//上传服务器的参数
        var temp = {
            date: dateFormat("YYYY-mm-dd", new Date())
        };
        return temp;
    },
    columns: [{
        field: 'domainId',
        title: 'Domain Id'
    }, {
        field: 'name',
        title: 'Name'
    }, {
        field: 'email',
        title: 'Email'
    }, {
       field: 'role',
       title: 'Role',
        formatter: function(value, row, index) {
            return "INTERN";
        }
    }, {
        field: 'internPeriodFromDate',
        title:'Start Time'
    }, {
       field: 'internPeriodToDate',
       title: 'End Time'
    }, {
        title: 'Team',
        formatter: function(value, row, index) {
            if (row.team == null) {
                return ''
            }
            let teamId = row.team.teamId;
            return teamId === null ? '': teamId;
        }
    }, {
        field: 'opt',
        title: ' ',
        sortable: false,
        width: '50',
        formatter: function(value, row, index) {
            return '<i class="glyphicon glyphicon-pencil btn-edit" domainId="'+ row.domainId +'"></i>' ;
        }
    }],
});

/**
 * 初始化team下拉框
 */
let initTeamList = function () {
  let teamBox = $('#teamIdSelector');
  teamBox.empty();
  let teamListStr = '';
  for (let i = 0; i < teams.length; i++) {
      teamListStr += '<option value="'+ i +'">' + teams[i].teamId +'</option>'
  }
  teamBox.append(teamListStr);
};

/**
 * 获得所有team
 */
let getTeams = function() {
    $.ajax({
        url: '/profile/teams',
        type: 'get',
        dataType: 'json',
        contentType: 'application/json;charset=UTF-8',
        success: function (data) {
            teams = data;
            initTeamList();
        },
        error: function () {
            Alert.commonError();
        }
    })
};
$(function () {
    var picker1 = $('#startTime').datetimepicker({
        format: 'YYYY-MM-DD',
        locale: moment.locale('zh-cn'),
        defaultDate: nowDateStr,
        //minDate: '2016-7-1'
    });
    var picker2 = $('#endTime').datetimepicker({
        format: 'YYYY-MM-DD',
        locale: moment.locale('zh-cn'),
        defaultDate: nowDateStr
    });
    //动态设置最小值
    picker1.on('dp.change', function (e) {
        picker2.data('DateTimePicker').minDate(e.date);
    });
    //动态设置最大值
    picker2.on('dp.change', function (e) {
        picker1.data('DateTimePicker').maxDate(e.date);
    });
    getTeams();
});