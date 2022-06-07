<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<style>
    <%@include file='/resources/css/table_dark.css' %>
</style>
<html>
<body>
<img src="<c:url value="/resources/images/jbs-logo.bmp" />" alt="image" />
<img src ="${pageContext.request.contextPath}/resources/images/123.png" />

<h4><a href="${pageContext.request.contextPath}/index">Main </a></h4>
<b>Today is:
    <fmt:timeZone value = "GMT">
        <fmt:formatDate type = "both" dateStyle = "medium" timeStyle = "short" value = "<%= new java.util.Date()%>" />
    </fmt:timeZone>
    (UTC +0)
</b>

<h4><a href="${pageContext.request.contextPath}/logout">Logout</a></h4>
<br>
</body>
</html>


