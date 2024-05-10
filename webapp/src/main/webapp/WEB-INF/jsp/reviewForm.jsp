<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <title><spring:message code="review.title"/></title>
</head>
<body>
<c:url value="/book/${bookId}/review/${userId}" var="postUrl"/>
<form:form
    action="${postUrl}"
    method="post"
    modelAttribute="reviewForm"
>
<h1>TODO</h1>
</form:form>
</body>
</html>
