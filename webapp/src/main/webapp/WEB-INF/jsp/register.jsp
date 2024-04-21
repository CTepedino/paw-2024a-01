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
        <c:url value="/register" var="postUrl"/>
        <form:form
            modelAttribute="registerForm"
            action="${postUrl}"
            method="post"
            enctype="multipart/form-data"
            cssClass="z-depth-2"
        >
            <h5 class="center-align">Register your new Account!</h5>

            <div class="row">
                <div class="input-field col s6">
                    <form:label path="firstName">First Name</form:label>
                    <form:input path="firstName" type="text"/>
                    <form:errors path="firstName" element="p"/>
                </div>
                <div class="input-field col s6">
                    <form:label path="lastName">Last Name</form:label>
                    <form:input path="lastName" type="text"/>
                    <form:errors path="lastName" element="p"/>
                </div>
            </div>
            <div class="row">
                <div class="input-field col s12">
                    <form:label path="email">Email Address</form:label>
                    <form:input path="email" type="text"/>
                    <form:errors path="email" element="p"/>
                </div>
            </div>
            <div class="row">
                <div class="input-field col s12">
                    <form:label path="password">Password</form:label>
                    <form:input path="password" type="password"/>
                    <form:errors path="password" element="p"/>
                </div>
            </div>
            <div class="input-field center-align">
                <button class="btn waves-effect waves-light" type="submit" name="action">
                    Register
                </button>
            </div>
        </form:form>
    </div>
</div>


</body>
</html>
