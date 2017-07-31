<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/jsp/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>

    <script type="text/javascript">



      function tt(){
          window.history.go(-1)
      };

<%--alert(${row.userName});--%>
    </script>
</head>
<body>
<%--<div id="gloobal">--%>
    <%--<h4>创建成功</h4>--%>
    <%--<p>--%>
    <%--<h5>详情：</h5>--%>
    <%--姓名：${user.userName}<br /> 年龄：${user.userage}<br /> 密码：${user.userEmail}<br />--%>
    <%--</p>--%>
<%--</div>--%>

<img src="<%=path%>${imagesPath}">
<div>
<table class="table1" style="border-collapse: collapse;width: 820px">
<thead>
<tr>

    <td style="width: 120px">邮箱</td>
    <td style="width: 75px;">姓名</td>
    <td style="width: 75px;">年龄</td>
</tr>
</thead>
<tbody>

<c:forEach items="${imagesPathList}" var="image">
    <img src="${basePath}${image}"><br/>
</c:forEach>

<c:forEach items="${userList}" var="row" varStatus="vs">
<tr class="">

    <td >
        <c:out value="${row.userEmail}"/>
    </td>
    <td>
        <c:out value="${row.userName}"/>
    </td>
    <td>
        <c:out value="${row.user_age}"/>
    </td>
    </tr>
    </c:forEach>
</tbody>
</table>
</div>



<div>
    <button  class="ff" id="ff" onclick="tt()" type="button" value="返回"/>返回
</div>
</body>


</html>