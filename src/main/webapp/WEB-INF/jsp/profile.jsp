<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<style>
    <%@include file='/resources/css/general.css' %>
</style>
<html>
<head>
    <title>Profile</title>
    <%@ include file="/WEB-INF/parts/header.jsp" %>
</head>

<body>
<table class="general">

    <thead>
    <tr>
        <th colspan="4" id="user">Hi, ${user.name}.<br><br>Here you have list of active aircraft.<br><br></th>
    </tr>
    <tr>
        <th>ID</th>
        <th>REGISTRATION</th>
        <th>TYPE</th>
        <th>AIRLINE</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${acftList}" var="list">
        <tr>
            <td>${list.id}</td>
            <td>${list.registration}</td>
            <td>${list.type}</td>
            <td>${list.airlineName}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
