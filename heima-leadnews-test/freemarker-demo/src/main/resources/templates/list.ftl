<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Hello World!</title>
</head>
<body>

<#-- list 数据的展示 -->
<b>展示list中的stu数据:</b>
<br>
<br>
<table>
    <tr>
        <td>序号</td>
        <td>姓名</td>
        <td>年龄</td>
        <td>钱包</td>
    </tr>
    <#--
        <#list指令 ： 遍历List集合
             stus ：集合数据在Model中的key
             as :  关键字，固定的
             stu : 正在遍历的集合的元素
     -->
    <#list stus as stu>
        <#if stu.name == '小红'>
            <tr style="background-color:red;">
                <#-- stu_index ：元素_index，获取元素对应的下标 -->
                <td>${stu_index+1}</td>
                <td>${stu.name}</td>
                <td>${stu.age}</td>
                <td>${stu.money}</td>
            </tr>
            <#else>
                <tr>
                    <#-- stu_index ：元素_index，获取元素对应的下标 -->
                    <td>${stu_index+1}</td>
                    <td>${stu.name}</td>
                    <td>${stu.age}</td>
                    <td>${stu.money}</td>
                </tr>
        </#if>
    </#list>

</table>
<hr>

<#-- Map 数据的展示 -->
<b>map数据的展示：</b>
<br/><br/>
<a href="###">方式一：通过 map['keyname'].property</a><br/>
输出stu1的学生信息：<br/>
姓名：${stuMap['stu1'].name}<br/>
年龄：${stuMap['stu1'].age}<br/>
<br/>
<a href="###">方式二：通过map.keyname.property</a><br/>
输出stu2的学生信息：<br/>
姓名：${stuMap.stu2.name}<br/>
年龄：${stuMap.stu2.age}<br/>

<br/>

<a href="###">遍历map中两个学生信息：</a><br/>
<table>
    <tr>
        <td>序号</td>
        <td>姓名</td>
        <td>年龄</td>
        <td>钱包</td>
    </tr>
    <#--
        stuMap?keys ：获取stuMap中所有key
        key : 正在遍历的key,可以自己取名字
    -->
    <#list stuMap?keys as key >
        <tr>
            <td>${key_index}</td>
            <td>${stuMap[key].name}</td>
            <td>${stuMap[key].age}</td>
            <td>${stuMap[key].money}</td>
        </tr>
    </#list>
</table>

<hr>
    <#if (date1?date >= date2?date)>
        条件成立
    </#if>

<hr>

<#if date1??>
    date1存在
</#if>

<#if date3??>
<#else>
    date3不存在
</#if>

<hr/>
${name ! ''}  <br>
${name2 ! 'name2不存在，给一个默认值'}

<#if (age>20)>
    年龄大于20
</#if>

</body>
</html>