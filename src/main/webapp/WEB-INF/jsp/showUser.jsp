<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Add User From</title>
</head>
<body>
<%--<form action="<c:url value='/user/save'/> " method="post">--%>


    <form action="/user/showUser" method="post">
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
            <label>密码：</label> <input type="text" id="pwd" name="userEmail"
                                      tabindex="3">
        </p>
        <p id="buttons">
            <input id="reset" type="reset" tabindex="4" value="取消">
            <input id="submit" type="submit" tabindex="5" value="创建">

        </p>
    </fieldset>
</form>
<div class="export_btn"><a href="javascript:exportData();">导出</a></div></td>



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

</script>
</body>
</html>