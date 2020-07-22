import {Alert} from "./alert.js";
let teams = [];
let nowDateStr =  dateFormat("YYYY-mm-dd", new Date());
let reg = new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$");
let user = {};
let currentUser = [];

$('#internCheckBox').change(function () {
   if(this.checked){
       $('.leader-select').css('display', 'block');
   } else {
       $('.leader-select').css('display', 'none');
   }
});
$(document).on("click", ".btn-edit", function () {
    $('.edit-user').addClass('update-user').removeClass("create-ser");
    $("#myModalLabel").text("Edit User");
    getInternByDomainId($(this).attr("domainId"));
    $('#domainId').val(currentUser.domainId);
    $('#domainId').attr('disabled', 'disabled');
    $('#name').val(currentUser.name);
    $('#email').val(currentUser.email);
    $("#fromTime").val(currentUser.internPeriodFromDate);
    $("#toTime").val(currentUser.internPeriodToDate);
    if (currentUser.team !== null) {
        for (let i = 0; i < teams.length; i++) {
            console.log(teams[i].teamId);
            if (teams[i].teamId === currentUser.team.teamId) {
                $('#teamId').val(i);
                break;
            }
        }
    }
    $("#myModal").modal("show");
});
$(document).on("click", ".btn-create", function () {
    $('.edit-user').addClass('create-user').removeClass("update-user");
    //初始化form
    $('#domainId').val('');
    $('#domainId').attr('disabled', false);
    $('#name').val('');
    $('#email').val('');
    $("#fromTime").val(nowDateStr);
    $("#myModalLabel").text("Create User");
    $("#myModal").modal("show");

});
$('#domainId, #name, #email').focus(function () {
    $(this).parent().parent().removeClass("has-error");
    $(this).next().text('');
})

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

let generateUser = function () {
    let domainIdEl = $('#domainId');
    let domainId = domainIdEl.val();
    if (domainId === '' || domainId === null) {
        domainIdEl.parent().parent().addClass("has-error");
        domainIdEl.next().text("Domain Id can not be null !");
        return false;
    }

    let nameEl = $('#name');
    let name = nameEl.val();
    if (name === '' || name === null) {
        nameEl.parent().parent().addClass("has-error");
        nameEl.next().text("Name can not be null !")
        return false;
    }

    let emailEl = $('#email');
    let email = emailEl.val();
    if (email === '' || email === null) {
        emailEl.parent().parent().addClass("has-error");
        emailEl.next().text("Email can not be null !")
        return false;
    }
    if (!reg.test(email)) {
        emailEl.parent().parent().addClass("has-error");
        emailEl.next().text("The email format is not correct !")
        return false;
    }
    let teamIdIndex = $('#teamId').val();
    let fromTime = $('#fromTime').val();
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

let operateIntern = function(user, url) {
    $.ajax({
        url: url,
        type: 'post',
        dataType: 'json',
        data: JSON.stringify(user),
        contentType: 'application/json;charset=UTF-8',
        // async: false,
        success: function (data) {
            $('#table').bootstrapTable('refresh');
            $("#myModal").modal("hide");
            if (url === '/profile/createIntern') {
                Alert.createSuccess();
            } else {
                Alert.updateSuccess();
            }

        },
        error: function (XMLHttpRequest) {
            if (url === '/profile/createIntern') {
                Alert.createFail();
            } else {
                Alert.updateFail();
            }
        }
    });
};


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
        error: function (XMLHttpRequest) {
            Alert.commonError();
        }
    })
};


$('#table').bootstrapTable({
    url: '/profile/getInterns',
    pagination: false,
    pageSize: 5,
    search: false,
    striped: true,
    sidePagination : 'server',
    pageList : [ 5, 10, 20],
    // searchText: '搜索',
    showSearchButton: true,
    responseHandler: function(res) {
        console.log(res);
        res.total = 100;
        res.rows = res.data;
        return res;
    },
    queryParams : function(params) {//上传服务器的参数
        var temp = {
            offset :params.offset + 0,// SQL语句起始索引
            pageNumber : params.limit,  // 每页显示数
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

let initTeamList = function () {
  let teamBox = $('#teamId');
  teamBox.empty();
  let teamListStr = '';
  for (let i = 0; i < teams.length; i++) {
      teamListStr += '<option value="'+ i +'">' + teams[i].teamId +'</option>'
  }
  teamBox.append(teamListStr);
};

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
        error: function (XMLHttpRequest) {
            Alert.commonError();
        }
    })
};
$(function () {
    var picker1 = $('#startTime').datetimepicker({
        format: 'YYYY-MM-DD',
        locale: moment.locale('zh-cn'),
        defaultDate: nowDateStr
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
    console.log($('#role').val())
});