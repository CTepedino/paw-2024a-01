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
    <input name="writerId" value="${writerId}" hidden="hidden">
    <div>
        <label>
            First Name:
            <input name="name" placeholder="First Name" required/>
        </label>
    </div>
    <div>
        <label>
            Last Name:
            <input name="lastName" placeholder="Last Name" required/>
        </label>
    </div>
    <div>
        <label>
            Email:
            <input name="email" placeholder="email@example.com" required/>
        </label>
    </div>
    <div>
        <input type="submit" value="Send Contact Information"/>
    </div>
</form>
</body>
</html>
