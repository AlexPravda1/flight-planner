<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
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

<h4 class="table_dark"> Today is day ${time.getDayOfMonth()} of the ${time.getMonth()} month. <br>
Current local time is around ${time.getHour()} : ${time.getMinute()}, more or less. <br><br>
    Here you have list of active aircraft: </h4>

<table class="table_dark">
    <thead>
    <tr>
        <th>ID</th>
        <th>EMAIL</th>
        <th>NAME</th>
        <th>SURNAME</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${users}" var="user">
        <tr>
            <td>${user.id}</td>
            <td>${user.email}</td>
            <td>${user.name}</td>
            <td>${user.surname}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>

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
            <td>${list.airline.name}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
