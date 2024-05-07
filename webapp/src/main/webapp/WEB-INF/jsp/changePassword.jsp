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

<c:set var="hideSearchBar" value="${true}" scope="request"/>
<jsp:include page="components/topBar.jsp"/>

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
                <spring:message code="session.changePasswordTitle"/>
            </h5>

            <div class="input-field">
                <form:label path="oldPassword">
                    <spring:message code="session.oldPassword"/><span class="red-text">*</span>
                </form:label>
                <form:input path="oldPassword" type="password"/>
            </div>
            <form:errors path="oldPassword" cssClass="red-text" element="p"/>

            <div class="input-field">
                <form:label path="password">
                    <spring:message code="session.newPassword"/><span class="red-text">*</span>
                </form:label>
                <form:input path="password" type="password" onkeyup="repeatPasswordCheck()"/>
                <span class="material-icons password-toggle-btn" onclick="togglePasswordVisibility()">visibility_off</span>
            </div>
            <form:errors path="password" cssClass="red-text" element="p"/>

            <div class="input-field">
                <form:label path="repeatPassword">
                    <spring:message code="session.repeatNewPassword"/><span class="red-text">*</span>
                </form:label>
                <form:input path="repeatPassword" type="password" onkeyup="repeatPasswordCheck()"/>
                <span class="material-icons password-toggle-btn" onclick="toggleRepeatPasswordVisibility()">visibility_off</span>

            </div>
            <form:errors path="repeatPassword" cssClass="red-text" element="p"/>

            <div class="input-field center-align submit-btn">
                <button class="btn waves-effect waves-light white-text" id="submitBtn" type="submit" name="action" disabled="disabled">
                    <spring:message code="session.changePassword"/>
                </button>
            </div>
        </form:form>
    </div>
</div>

<script src="<c:url value="/js/togglePasswordView.js"/>"></script>
<script src="<c:url value="/js/repeatPasswordCheck.js"/>"></script>
</body>
</html>