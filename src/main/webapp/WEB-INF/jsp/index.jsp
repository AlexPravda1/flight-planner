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
    <tr><td><a href="${pageContext.request.contextPath}/welcome">User profile with aircraft list</a><br></td></tr>
    <tr><td><a href="${pageContext.request.contextPath}/flights">Display All Flights</a></td></tr>
    <tr><td><a href="${pageContext.request.contextPath}/flights?hasFiles=true">Display All Flights with Files</a></td></tr>
    <tr><td><a href="${pageContext.request.contextPath}/flights?hasNotes=true">Display All Flights with Notes</a></td></tr>
    <tr><td><a href="${pageContext.request.contextPath}/flights?hasNotes=true&hasFiles=true">Display All Flights with Notes&Files</a></td></tr>
    <tr><td><a href="${pageContext.request.contextPath}/flights?registration=2-SWIS">Display All Flights for 2-SWIS aircraft</a></td></tr>
    <tr><td><a href="${pageContext.request.contextPath}/flights?hasNotes=true&registration=2-SWIS">Display All Flights with Notes for 2-SWIS aircraft</a></td></tr>
    <tr><td><a href="${pageContext.request.contextPath}/flights?daysRange=5">Display All Flights for 5 days ahead only</a></td></tr>
</table>
</html>
