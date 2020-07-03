let Alert = {
    applySuccess: function () {
        Swal.fire("申请签到成功!", "", "success")
    },
    cancelSuccess: function () {
        Swal.fire("取消签到成功!", "", "success")
    }
};

export {Alert};