<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title><c:out value="${title}"/></title>
</head>

<body>
<%@ include file="components/topBar.jsp" %>
    <h1>Page Using TagLib</h1>
    <p>Username: <c:out value="${user.username}"/></p>
    <c:if test="${user.userId > 1}">
        <p>Id is bigger than 1</p>
    </c:if>
</body>

</html>