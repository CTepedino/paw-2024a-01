<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
    <title>Contact Information</title>
    <link href="/css/style.css" rel="stylesheet"/>
</head>
<body>
<c:url value="/sendBuyInfo" var="registerUrl"/>
<form action="${registerUrl}" method="post">
    <div>
        <label>
            Name:
            <input name="username" placeholder="username"/>
        </label>
    </div>
    <div>
        <label>
            Email:
            <input name="email" placeholder="email@example.com"/>
        </label>
    </div>
    <div>
        <input type="submit" value="Send Contact Information"/>
    </div>
</form>
</body>
</html>
