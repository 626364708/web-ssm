<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/taglib.jsp"%>
<%--<%--%>
    <%--String path = request.getContextPath();--%>
    <%--String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";--%>
<%--%>--%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <title>Add User From</title>
</head>
<body >
<form action="<c:url value='/user/save'/> " method="post">
    <%--<form action="/user/showUser" method="post">--%>
    <fieldset>
        <legend>创建用户</legend>
        <p>
            <label>姓名：</label> <input type="text" id="name" name="userName"
                                      tabindex="1">
        </p>
        <p>
            <label>年龄：</label> <input type="text" id="age" name="age"
                                      tabindex="2">
        </p>
        <p>
            <label>密码：</label> <input type="text" id="pwd" name="userPwd"
                                      tabindex="3">
        </p>


        <p id="buttons">
            <input id="reset" type="reset" tabindex="4" value="取消">
            <input id="submit" type="submit" tabindex="5" value="创建">


        </p>
    </fieldset>
</form>


<form action="getJson" method="get">
    <input type="submit" value="点击获取新闻JSON数据"/>
</form>



<form name="fmUpload" action="fileup/test" method="post" enctype="multipart/form-data">
    pic:<input type="file" name="pic"><br>
    <input type="submit" value=" 文件上传 " ><br>
</form>

<div class="export_btn"><a href="javascript:exportData();">导出</a></div></td>


<div class="ch" >
    <form name="fmUpload" action="fileup/test2" method="post" enctype="multipart/form-data">
        pic:<input type="file" name="pic"><br>
        <input type="submit" value=" 文件上传 " ><br>
    </form>


</div>
<img src="http://bpic.588ku.com/back_pic/00/05/05/995625dd8ca6ffd.jpg!/fh/300/quality/90/unsharp/true/compress/true"/>
<div>



    <c:forEach items="${list}" var="row" varStatus="vs">
        <tr class="">

            <td >
                <c:out value="${row.a}"/>
            </td>
            <td>
                <c:out value="${row.b}"/>
            </td>
            <td>
                <c:out value="${row.c}"/>
            </td>
        </tr>
    </c:forEach>

</div>
<script type="text/javascript">

    function exportData(){
        window.location.href ="<%=path%>/global/export?lx=TJ";
    }
//    function Httpget(){
//    $.ajax({
//        type:'GET',
//        URL:'http://localhost:8080/Users/User1',
//        ASYNC:true,
//        data:{
//            'userId':$("#userid2").val()
//        },
//        dataType:'json',
//        success:function (data){
//            $("#p_data").html(JSON.stringify(data));
//            alert($("#userid2").val())
//        },
//        error:function (msg) {
//            alert("与服务器连接断开。。。" + JSON.stringify(msg));
//        }
//    })















</script>
</body>
</html>