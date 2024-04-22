<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Sign In</title>
    <link href="${pageContext.request.contextPath}/css/userForm.css" rel="stylesheet"/>
</head>
<%@ include file="components/topBar.jsp" %>
<body>
<div class="small-container">
    <div class="form">
        <c:url value="/login" var="postUrl"/>
        <form
                action="${postUrl}"
                method="post"
                class="z-depth-2"
        >
            <h5 class="center-align">Login to Your Account</h5>
            <div class="input-field">
                <input id="email" type="text" class="validate" name="email">
                <label for="email">Email</label>
            </div>
            <div class="input-field">
                <input id="password" type="password" class="validate" name="password">
                <label for="password">Password</label>
            </div>
            <div>
                <label>
                    <input type="checkbox"/>
                    <span>Remember Me</span>
                </label>
            </div>
            <div class="input-field center-align">
                <button class="btn waves-effect waves-light" type="submit" name="action">
                    Sign In
                </button>
            </div>
            <p class="center-align">Don't have an account yet? <a href="${pageContext.request.contextPath}/signup">Sign up</a></p>
        </form>
    </div>
</div>
</body>
</html>
