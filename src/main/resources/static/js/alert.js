let Alert = {
    applySuccess: function () {
        Swal.fire("申请签到成功!", "", "success")
    },
    cancelSuccess: function () {
        Swal.fire("取消签到成功!", "", "success")
    },
    reApplySuccess: function () {
        Swal.fire("重新签到成功!", "", "success")
    },
    approveSuccess: function () {
        Swal.fire("审批成功!", "", "success")
    },
    rejectSuccess: function () {
        Swal.fire("拒批成功!", "", "success");
    },
    commonError: function () {
        Swal.fire("服务器发生错误!", "", "error");
    },
    createSuccess: function () {
        Swal.fire("创建成功!", "", "success");
    },
    createFail: function () {
        Swal.fire("创建失败!", "", "error");
    },
    updateFail: function () {
        Swal.fire("修改失败!", "", "error");
    },
    updateSuccess: function () {
        Swal.fire("修改成功!", "", "success");
    },
    rejectFutureSign :function () {
        Swal.fire("不能对未来的日期签到!","", "error")
    },
    error: function (error) {
        Swal.fire(error, "", "error");
    }
};

export {Alert};