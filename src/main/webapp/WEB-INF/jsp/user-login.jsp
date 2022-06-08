<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<style>
    <%@include file='/resources/css/general.css' %>
</style>
<html>
<head>
    <title>JBS Login page</title>
    <%@ include file="/WEB-INF/parts/header.jsp" %>
</head>
<body>
<table class="general">
    <tr>
        <th><h3>Please enter username and password</h3></th>
    </tr>
</table>
<form method ="POST" action="/auth" class="general">
    <table>
        <tr style="color: red;">
            <td></td>
            <td>${SPRING_SECURITY_LAST_EXCEPTION.message}</td>
            <td><c:out value="${exception.message}" /></td>
            <td><c:out value="${pageContext.exception.message}" /></td>

        </tr>
        <tr>
            <td>Login:</td>
            <td><label for="login"></label>
                <input id="login" name="login" type="text" ></td>
        </tr>
        <tr>
            <td>Password:</td>
            <td><label for="password"></label>
                <input id="password" name="password" type="password" ></td>
        </tr>
        <tr>
            <td></td>
            <td><input type="submit" value="Login" id="loginSubmit"></td>
        </tr>
    </table>
</form>
</body>
</html>
