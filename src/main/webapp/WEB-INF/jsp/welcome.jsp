<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<style>
    <%@include file='/resources/css/table_dark.css' %>
</style>
<html>
<head>
    <title>Lets begin</title>
</head>

<body>
<h1 class="table_dark"> Greetings, ${userName}! </h1> <br>

<h4 class="table_dark"> Today is:
    <fmt:timeZone value = "GMT">
        <fmt:formatDate type = "both" dateStyle = "medium" timeStyle = "short" value = "<%= new java.util.Date()%>" />
    </fmt:timeZone>
    (UTC +0)
    Here you have list of active aircraft: </h4>

<table class="table_dark">
    <thead>
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
