<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<style>
    <%@include file='/resources/css/menu.css' %>
</style>
<html>
<body>
<logo>
<img src="<c:url value="/resources/images/logo.png" />"/>
</logo>
<nav>
    <a href="${pageContext.request.contextPath}/">Home</a>
    <a href="${pageContext.request.contextPath}/profile">Profile</a>
    <a href="https://jbs.aero/contactus">Contacts</a>
    <a href="https://jbs.aero/contents/lists/about-us">About JBS</a>
    <a href="${pageContext.request.contextPath}/logout">Logout</a>
    <div class="animation start-home"></div>
</nav>
<BR><BR><BR><BR><BR>

</body>
</html>


