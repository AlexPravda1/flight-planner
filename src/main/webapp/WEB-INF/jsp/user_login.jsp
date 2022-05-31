<!DOCTYPE html>
<html>
<head>
    <title>Spring Security- Change default login page</title>
</head>
<body>
<h1>Spring Security- Change default login page</h1>

<form action="login" method="post">
    <table>
        <tr style="color: red;">
            <td></td>
            <td>${SPRING_SECURITY_LAST_EXCEPTION.message}</td>
        </tr>
        <tr>
            <td>User name:</td>
            <td><input id="username" name="username" type="text">
        </tr>
        <tr>
            <td>Password:</td>
            <td><input id="password" name="password" type="password"></td>
        </tr>
        <tr>
            <td></td>
            <td><input type="submit" value="Login"></td>
        </tr>
    </table>
</form>
</body>
</html>
