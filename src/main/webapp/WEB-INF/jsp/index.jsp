<%@ page contentType="text/html;charset=UTF-8" %>
<style>
    <%@include file='/resources/css/table_dark.css' %>
</style>
<html>
<head>
    <title>JBS Customer Portal</title>
</head>

<%@ include file="/WEB-INF/parts/header.jsp" %>

<form method="post" id="redirect"></form>
<h1 class="table_dark"> Greetings, ${userName}! </h1>
<table class="table_dark">
    <tr>
        <th>Redirect to</th>
    </tr>
    <tr><td><a href="${pageContext.request.contextPath}/flights">Display All Flights</a></td></tr>
    <tr><td><a href="${pageContext.request.contextPath}/flights?hasFiles=true">Display All Flights with Files</a></td></tr>
</table>
</html>
