
$('#internCheckBox').change(function () {
   if(this.checked){
       $('.leader-select').css('display', 'block');
   } else {
       $('.leader-select').css('display', 'none');
   }
});
$(document).on("click", ".btn-edit", function () {
    $("#myModal").modal("show");
});

$(document).on("click", ".save-user", function () {
   console.log("创建用户成功");
   let domainId = $('#domainId').val();
   let name = $('#name').val();
   let email = $('#email').val();
   let teamId = $('#teamId').val();
   console.log(domainId);
    console.log(name);
    console.log(email);
    console.log(teamId);
    $("#myModal").modal("hide");
});

$('#table').bootstrapTable({
    url: '/mock/users',
    pagination: true,
    pageSize: 5,
    search: true,
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
            pageNumber : params.limit  // 每页显示数量
        };
        console.log(params);
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
       title: 'Role'
    }, {
        field: 'opt',
        title: 'Operation',
        sortable: false,
        width: '90',
        formatter: function(value, row, index) {
            return "<i class='glyphicon glyphicon-pencil btn-edit'></i>" +
                "<i class='glyphicon glyphicon-trash'></i>";
        }
    }],
});