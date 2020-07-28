/**
 * 直接提交表单
 */
$(function () {
   submitLoginForm();
});

/**
 * 点击登录按钮提交表单
 */
$("#loginButton").on("click",function() {
    submitLoginForm()
});

/**
 * 提交表单
 */
function submitLoginForm() {
    let userNameElement = $("#username")
    let username = userNameElement.val().trim();
    if (username === "" || username == null) {
        userNameElement.parent().parent().addClass("has-error");
        userNameElement.next().text("User name can not be empty")
        return;
    }
    let passwordElement = $("#password")
    let password = passwordElement.val().trim();
    if (password === "" || password == null) {
        passwordElement.parent().parent().addClass("has-error");
        passwordElement.next().text("Password  can not be empty");
        return;
    }
    $.ajax(
        {
            url: "/login/form",
            data: {"username": username, "password": password},
            type: "POST",
            dataType: "json",
            success: function (data) {
                if (data === 0) {
                    window.location.href ="/index";
                } else {
                }
            },
            error: function (XMLHttpRequest){
                console.log(XMLHttpRequest.responseText);
            }
        });
}

