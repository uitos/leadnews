<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>inner Function</title>
</head>
<body>

<b>获得集合大小</b><br>
<hr>
集合大小：${stus?size}
<hr>


<b>获得日期</b><br>
<hr>
显示年月日: ${today?date}       <br>
<hr>
显示时分秒：${today?time}<br>
<hr>
显示日期+时间：${today?datetime}<br>
<hr>
自定义格式化：  ${today?string("yyyy年MM月")}<br>

<hr>

<b>内建函数C</b><br>
<hr>
没有C函数显示的数值：${point} <br>
<hr>
有C函数显示的数值：${point?c}

<hr>

<b>声明变量assign</b><br>
<hr>
<#assign text="{'bank':'工商银行','account':'10101920201920212'}" />
<#assign data=text?eval />
开户行：${data.bank}  账号：${data.account}

<hr>
</body>
</html>