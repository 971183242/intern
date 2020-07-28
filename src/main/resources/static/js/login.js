import {Alert} from "./alert.js";
/**
 * 初始化背景图
 */
$(function () {
    var url = "url(/img/login-bg/bg-" + getRandomNumBoth(1,6) + ".jpg)";
    $(".login-body").css("background", url)
});
/**
 * 密码输入框 按enter可提交表单
 */
$("#password").keydown(function (e) {
    if(e.keyCode === 13) {
        submitLoginForm();
    }
});
/**
 * 点击登录按钮提交表单
 */
$(".login-btn").on("click",function() {
    submitLoginForm()
});

/**
 * 提交表单
 */
function submitLoginForm() {
    var username = $("#username").val().trim();
    console.log(username);
    if (username === "" || username == null) {
        Alert.error("DomainId is required");
        return;
    }
    var password = $("#password").val().trim();
    if (password === "" || password == null) {
        Alert.error("Password is required");
        return;
    }
    $.ajax(
        {
            url: "/login/form",
            data: {"username": username, "password": password},
            type: "POST",
            dataType: "json",
            success: function (data) {
                if (data.code === 1) {
                    window.location.href ="/index";
                } else {
                    Alert.error(data.msg);
                }
            },
            error: function () {
                Alert.commonError()
            }
        });
}
/**
 * 生成随机数
 */
function getRandomNumBoth(Min,Max){
    var Range = Max - Min;
    var Rand = Math.random();
    return Min + Math.round(Rand * Range);
}