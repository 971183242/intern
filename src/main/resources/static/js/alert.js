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
    }
};

export {Alert};