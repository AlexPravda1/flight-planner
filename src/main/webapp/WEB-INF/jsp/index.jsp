<%@ page contentType="text/html;charset=UTF-8" %>
<style>
    <%@include file='/resources/css/table_dark.css' %>
</style>
<html>
<head>
    <title>JBS Customer Portal</title>
</head>

<%@ include file="/WEB-INF/parts/header.jsp" %>

<h1 class="table_dark"> Greetings, ${user.name}! </h1>
<table class="table_dark">
    <tr>
        <th>Redirect to</th>
    </tr>
    <tr><td><a href="${pageContext.request.contextPath}/welcome">User profile with aircraft list</a><br></td></tr>
    <tr><td><a href="${pageContext.request.contextPath}/flights">Display All Flights</a></td></tr>
</table>

<form method="GET" id="redirect" action="${pageContext.request.contextPath}/flights" class="table_dark">
    <table>
        <tr>
            <th rowspan="4"><input type="submit" value="Filter flights"></th>
            <td><input type="checkbox" name="hasNotes" value="true"> Has notes</td></tr>
        <tr><td><input type="checkbox" name="hasFiles" value="true"> Has files</td></tr>
        <tr><td>Days limit: <input type="number" name="daysRange" value="90" min="0" max="90"></td></tr>
        <tr><td>Registration: <select name="registration">
                                <option value="">All</option>
                                <c:forEach items="${acftList}" var="list">
                                    <option value="${list.registration}">${list.registration}</option>
                                </c:forEach></select>
                                </td></tr>
    </table>
</form>

</html>
