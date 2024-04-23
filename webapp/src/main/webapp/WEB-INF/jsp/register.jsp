<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
    <meta charset="UTF-8">
    <title>Sign Up</title>
    <link href="${pageContext.request.contextPath}/css/userForm.css" rel="stylesheet"/>
</head>

<%@ include file="components/topBar.jsp" %>

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
            <h5 class="center-align">Create a Cybrary account</h5>
            <div class="input-field">
                <form:label path="email">Email Address</form:label>
                <form:input path="email" type="text"/>
                <form:errors path="email" element="p" cssClass="red-text"/>
            </div>

            <div class="input-field">
                <form:label path="password">Password</form:label>
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
                    Register
                </button>
            </div>

            <p class="center-align">Already have an account? <a href="${pageContext.request.contextPath}/signup">Log in</a></p>
        </form:form>
    </div>
</div>
<script src="<c:url value="/js/togglePasswordView.js"/>"></script>

</body>
</html>
