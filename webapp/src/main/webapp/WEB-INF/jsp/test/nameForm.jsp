<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<c:url value="/signup/writer" var="signUpUrl" />
<form:form
        action="${signUpUrl}"
        method="post"
        modelAttribute="writerNameForm"
>
    <form:label path="firstName">First name</form:label>
    <form:input path="firstName" type="text"/>
    <br>

    <form:label path="lastName">Last name</form:label>
    <form:input path="lastName" type="text"/>
    <br>

    <button type="submit">Sign up</button>
</form:form>
</body>
</html>
