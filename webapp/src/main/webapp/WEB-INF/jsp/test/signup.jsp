<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<c:url value="/signup" var="signUpUrl"/>
<form:form
    action="${signUpUrl}"
    method="post"
    modelAttribute="signUpForm"
>

    <form:label path="email">Email</form:label>
    <form:input path="email" type="email"/>
    <br>

    <form:label path="password">Password</form:label>
    <form:input path="password" type="password"/>
    <br>

    <button type="submit">Sign Up</button>
</form:form>
</body>
</html>
