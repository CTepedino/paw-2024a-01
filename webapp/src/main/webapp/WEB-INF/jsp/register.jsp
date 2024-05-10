<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><spring:message code="session.signup"/></title>
    <link href="${pageContext.request.contextPath}/css/userForm.css" rel="stylesheet"/>
</head>


<c:set var="hideSearchBar" value="${true}" scope="request"/>
<%--<jsp:include page="components/topBar.jsp"/>--%>
<jsp:include page="components/topBar.jsp">
    <jsp:param name="hasWriterRole" value="${isWriter}" />
    <jsp:param name="hideSearchBar" value="${true}"/>
</jsp:include>


<%@include file="components/materializeComponent.jsp"%>
<body>
<%@ include file="components/topBar2.0.jsp" %>
<div class="small-container">
    <div class="form">
        <c:url value="/signup" var="postUrl"/>

        <form:form
            modelAttribute="signUpForm"
            action="${postUrl}"
            method="post"
            enctype="multipart/form-data"
            cssClass="z-depth-2"
        >
            <h5 class="center-align">
                <spring:message code="session.signupTitle"/>
            </h5>

            <div class="input-field">
                <form:label path="firstName">
                    <spring:message code="book.addBook.writerFirstName"/><span class="red-text">*</span>
                </form:label>
                <form:input type="text" path="firstName"/>
            </div>
            <form:errors path="firstName" element="p" cssClass="red-text err-msj"/>

            <div class="input-field">
                <form:label path="lastName">
                    <spring:message code="book.addBook.writerLastName"/><span class="red-text">*</span>
                </form:label>
                <form:input type="text" path="lastName"/>
            </div>
            <form:errors path="lastName" element="p" cssClass="red-text err-msj"/>

            <div class="input-field">
                <form:label path="email">
                    <spring:message code="session.email"/><span class="red-text">*</span>
                </form:label>
                <form:input path="email" type="text"/>
            </div>
            <form:errors path="email" element="p" cssClass="red-text err-msj"/>

            <div class="input-field">
                <form:label path="password">
                    <spring:message code="session.password"/><span class="red-text">*</span>
                </form:label>
                <form:input path="password" type="password" id="password" name="password" onkeyup="repeatPasswordCheck()"/>
                <span class="material-icons password-toggle-btn" onclick="togglePasswordVisibility()">visibility_off</span>
            </div>
            <form:errors path="password" element="p" cssClass="red-text err-msj"/>

            <div class="input-field">
                <form:label path="repeatPassword">
                    <spring:message code="session.repeatPassword"/><span class="red-text">*</span>
                </form:label>
                <form:input id="repeatPassword" path="repeatPassword" type="password" name="repeatPassword" onkeyup="repeatPasswordCheck()"/>
                <span class="material-icons repeat-password-toggle-btn" onclick="toggleRepeatPasswordVisibility()">visibility_off</span>
            </div>

            <p class="red-text" id="passwordErrorMessage" style="visibility: hidden"><spring:message code="session.repeatPasswordError"/></p>

            <div class="input-field center-align submit-btn">
                <button class="btn waves-effect waves-light white-text" id="submitBtn" type="submit" name="action" disabled="disabled">
                    <spring:message code="session.signup"/>
                </button>
            </div>

            <p class="center-align">
                <spring:message code="session.toLogin"/>
                <a href="${pageContext.request.contextPath}/login">
                    <spring:message code="session.login"/>
                </a>
            </p>
        </form:form>
    </div>
</div>
<script src="<c:url value="/js/togglePasswordView.js"/>"></script>
<script src="<c:url value="/js/repeatPasswordCheck.js"/>"></script>
</body>
</html>
