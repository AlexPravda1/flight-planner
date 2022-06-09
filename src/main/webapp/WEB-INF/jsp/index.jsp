<%@ page contentType="text/html;charset=UTF-8" %>
<style>
    <%@include file='/resources/css/general.css' %>
</style>
<html>
<head>
    <title>JBS Customer Portal</title>
</head>
<%@ include file="/WEB-INF/parts/header.jsp" %>

<form method="GET" id="redirect" action="${pageContext.request.contextPath}/flights" class="general">
    <table>
        <tr><th colspan="2" id="user">Hi, ${user.name}.<br><br>Please make your selection and review the flights.<br><br></th></tr>
        <tr>
            <th rowspan="5"><input type="submit" value="Show flights" id="submit"></th>
        <tr><td><input type="checkbox" name="hasNotes" value="true"> With notes</td></tr>
        <tr><td><input type="checkbox" name="hasFiles" value="true"> With files</td></tr>
        <tr><td>Days limit: <input type="number" name="daysRange" value="90" min="0" max="90"></td></tr>
        <tr><td>Registration: <select name="registration">
                                <option value="All">All</option>
                                <c:forEach items="${acftList}" var="list">
                                    <option value="${list.registration}">${list.registration}</option>
                                </c:forEach></select>
                                </td></tr>
    </table>

</form>

</html>
