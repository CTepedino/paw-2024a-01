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
        <c:url value="/signup/writer" var="postUrl"/>
        <form:form
                modelAttribute="writerNameForm"
                action="${postUrl}"
                method="post"
                cssClass="z-depth-2"
        >
            <h5 class="center-align">Fill in your details!</h5>


            <div class="input-field">
                <form:label path="firstName">First Name</form:label>
                <form:input path="firstName" type="text"/>
                <form:errors path="firstName" element="p"/>
            </div>

            <div class="input-field">
                <form:label path="lastName">Last Name</form:label>
                <form:input path="lastName" type="text"/>
                <form:errors path="lastName" element="p"/>
            </div>

            <div class="input-field center-align">
                <button class="btn waves-effect waves-light" type="submit" name="action">
                    Submit
                </button>
            </div>
        </form:form>
    </div>
</div>


</body>
</html>
