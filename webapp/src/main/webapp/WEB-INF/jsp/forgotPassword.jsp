<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><spring:message code="session.changePassword"/></title>
    <link href="<c:url value="/css/userForm.css"/>" rel="stylesheet"/>

    <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/images/cybrary.png"/>"/>
</head>

<c:set value="${true}" scope="request" var="hideSearchBar"/>
<%@include file="components/topBar.jsp" %>

<body>
<div class="small-container">
    <div class="form">
        <c:url value="/forgotPassword" var="postUrl"/>
        <form:form
                modelAttribute="sendForgotPasswordEmailForm"
                action="${postUrl}"
                method="post"
                class="z-depth-2"
        >
            <h5 class="center-align"><spring:message code="session.forgotPasswordTitle"/></h5>

            <p class="center-align"><spring:message code="session.forgotPasswordInstructions"/></>

            <div class="input-field">
                <form:label path="email">
                    <spring:message code="session.email"/><span class="red-text">*</span>
                </form:label>
                <form:input path="email" type="text" maxlength="255"/>
                <form:errors cssClass="red-text" element="p" path="email"/>
            </div>

            <div class="input-field center-align submit-btn">
                <button class="btn waves-effect waves-light white-text" type="submit" name="action">
                    <strong><spring:message code="session.resetPassword"/></strong>
                </button>
            </div>

        </form:form>
    </div>
</div>
</body>

</html>
