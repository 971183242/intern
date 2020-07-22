<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Sending Email with Freemarker HTML Template Example</title>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <link href='http://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'>

    <!-- use the font -->
    <style>
        body {
            font-family: 'DengXian', sans-serif;
            font-size: 15px;
        }
    </style>
</head>
<body style="margin: 0; padding: 0;">
<table align="left" border="0" cellpadding="0" cellspacing="0" width="600" style="border-collapse: collapse;">
    <tr>
        <td>
            <p><b>Dear Wendy, Leonard, Jacky:</b></p>
        </td>
    </tr>
    <tr>&nbsp;</tr>
    <tr>
        <td>本期实习生签到记录报表如下：</td>
    </tr>
    <table border="1" cellpadding="0" cellspacing="0" style="border-collapse: collapse;">
    <tr>
            <th style="background: #7af6e9">实习生</th>
            <th style="background: #09ff74">已审批</th>
            <th style="background: #f80101">已拒绝</th>
            <th style="background: #ffff0d">待审批</th>
        </tr>
        <tbody id="report" align="left">
        <#list attendance as item>
            <tr>
                <td align="center">${item.internName}</td>
                <td align="right">${item.approvedDays}</td>
                <td align="right">${item.rejectedDays}</td>
                <td align="right">${item.checkInDays}</td>
            </tr>
        </#list>
        </tbody>
    </table>
    <tr>&nbsp&nbsp系统审批入口↓↓↓</tr>
    <tr>&nbsp&nbsp http://shagit02-w10.corp.oocl.com:9080/leader#</tr>
    <tr>&nbsp;</tr>
    <tr>
        <td>
            <p>Thanks and Regards
                <br>
                Intern System</p>
        </td>
    </tr>
</table>
<script type="text/javascript">
    var report = document.getElementById("report");
    var tr = report.getElementsByTagName('tr');
    for (var i = 0; i < tr.length; i++) {
        var td = tr[i].getElementsByTagName('td')[3];
        // if (td.innerText !== "0") {
            td.style.color = "#f6050f";
        // }
    }
</script>
</body>
</html>