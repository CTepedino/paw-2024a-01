<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><spring:message code="session.changePassword"/></title>
    <link href="<c:url value="/css/userForm.css"/>" rel="stylesheet"/>

    <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/images/cybrary.png"/>"/>
</head>


<c:set value="${true}" var="hideSearchBar" scope="request"/>
<%@include file="components/topBar.jsp" %>


<body>
<div class="small-container">
    <div class="form">
        <c:url value="/resetPassword/${id}/${code}" var="postUrl"/>
        <form:form
                modelAttribute="resetPasswordForm"
                action="${postUrl}"
                method="post"
                enctype="multipart/form-data"
                cssClass="z-depth-2"
        >
            <h5 class="center-align">
                <spring:message code="session.changePasswordTitle"/>
            </h5>

            <div class="input-field">
                <form:label path="password">
                    <spring:message code="session.newPassword"/><span class="red-text">*</span>
                </form:label>
                <form:input path="password" type="password" id="password" name="password" onkeyup="repeatPasswordCheck()" maxlength="255"/>
                <span class="material-icons password-toggle-btn" onclick="togglePasswordVisibility()">visibility_off</span>
            </div>
            <form:errors path="password" cssClass="red-text" element="p"/>

            <div class="input-field">
                <form:label path="repeatPassword">
                    <spring:message code="session.repeatNewPassword"/><span class="red-text">*</span>
                </form:label>
                <form:input path="repeatPassword" type="password" name="repeatPassword" id="repeatPassword" onkeyup="repeatPasswordCheck()"/>
                <span class="material-icons repeat-password-toggle-btn" onclick="toggleRepeatPasswordVisibility()">visibility_off</span>

            </div>

            <p class="red-text" id="passwordErrorMessage" style="visibility: hidden"><spring:message code="session.repeatPasswordError"/></p>

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