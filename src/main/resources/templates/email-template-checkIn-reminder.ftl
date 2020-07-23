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
            <p>Dear ${internId}</p>
        </td>
    </tr>
    <tr>&nbsp;</tr>
    <tr>&nbsp&nbsp您今日尚未签到，点击下方URL↓↓↓前往签到</tr>
    <tr>&nbsp&nbsp ${checkInUrl}</tr>
    <tr>&nbsp;</tr>
    <tr>
        <td>
            <p>Thanks and Regards
                <br>
                Intern System</p>
        </td>
    </tr>
</table>

</body>
</html>