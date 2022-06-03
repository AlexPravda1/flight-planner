<!DOCTYPE html>
<html>
<head>
    <title>Spring Security- Change default login page</title>
</head>
<body>

<h1>Spring Security- Change default login page</h1>

<form method ="POST" action="${pageContext.request.contextPath}/auth">
    <table>
        <tr style="color: red;">
            <td></td>
            <td>${SPRING_SECURITY_LAST_EXCEPTION.message}</td>
        </tr>
        <tr>
            <td>Login:</td>
            <td><label for="login"></label>
                <input id="login" name="login" type="text"></td>
        </tr>
        <tr>
            <td>Password:</td>
            <td><label for="password"></label>
                <input id="password" name="password" type="password"></td>
        </tr>
        <tr>
            <td></td>
            <td><input type="submit" value="Login"></td>
        </tr>
    </table>
</form>
</body>
</html>
