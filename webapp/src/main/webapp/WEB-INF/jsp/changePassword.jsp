<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><spring:message code="session.changePassword"/></title>
    <link href="${pageContext.request.contextPath}/css/userForm.css" rel="stylesheet"/>
</head>

<%@ include file="components/topBar.jsp" %>

<body>
<div class="small-container">
    <div class="form">
        <c:url value="/changePassword" var="postUrl"/>
        <form:form
                modelAttribute="passwordForm"
                action="${postUrl}"
                method="post"
                enctype="multipart/form-data"
                cssClass="z-depth-2"
        >
            <h5 class="center-align">
                Type your new password!
            </h5>


            <div class="input-field">
                <form:label path="email">
                    <spring:message code="session.email"/>
                </form:label>
                <form:input path="email" type="text"/>
                <form:errors path="email" element="p"/>
            </div>

            <div class="input-field">
                <form:label path="password">
                    <spring:message code="session.newPassword"/>
                </form:label>
                <form:input path="password" type="password"/>
                <form:errors path="password" element="p"/>
            </div>

            <div class="input-field center-align">
                <button class="btn waves-effect waves-light" type="submit" name="action">
                    <spring:message code="session.changePassword"/>
                </button>
            </div>
        </form:form>
    </div>
</div>


</body>
</html>
