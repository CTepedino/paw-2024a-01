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
<jsp:include page="components/topBar.jsp"/>


<body>
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
                <form:label path="email">
                    <spring:message code="session.email"/>
                </form:label>
                <form:input path="email" type="text"/>
                <form:errors path="email" element="p" cssClass="red-text"/>
            </div>

            <div class="input-field">
                <form:label path="password">
                    <spring:message code="session.password"/>
                </form:label>
                <form:input path="password" type="password" id="password"/>
                <span class="material-icons password-toggle-btn" onclick="togglePasswordVisibility()">visibility_off</span>
                <form:errors path="password" element="p" cssClass="red-text"/>
            </div>

<%--            <div class="input-field">
                <form:label path="repeatPassword">Confirm password</form:label>
                <form:input path="repeatPassword" type="password"/>
                <form:errors path="repeatPassword" element="p" cssClass="red-text"/>
            </div>--%>

            <div class="input-field center-align submit-btn">
                <button class="btn waves-effect waves-light white-text" type="submit" name="action">
                    <spring:message code="session.signup"/>
                </button>
            </div>

            <p class="center-align">
                <spring:message code="session.toLogin"/>
                <a href="${pageContext.request.contextPath}/signup">
                    <spring:message code="session.login"/>
                </a>
            </p>
        </form:form>
    </div>
</div>
<script src="<c:url value="/js/togglePasswordView.js"/>"></script>

</body>
</html>
