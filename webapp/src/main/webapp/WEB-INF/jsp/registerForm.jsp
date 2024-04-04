<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
    <title>Title</title>
    <link href="/css/style.css" rel="stylesheet"/>
</head>
<body>
<%@ include file="components/topBar.jsp" %>
<%@ include file="components/header.jsp" %>
<c:url value="/create" var="registerUrl"/>
<form action="${registerUrl}" method="post">
    <div>
        <label>
            Username:
            <input name="username" placeholder="username"/>
        </label>
    </div>
    <div>
        <input type="submit" value="Register!"/>
    </div>
</form>
</body>
</html>