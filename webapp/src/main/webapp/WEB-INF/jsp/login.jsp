<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link href="${pageContext.request.contextPath}/css/userForm.css" rel="stylesheet"/>
</head>

<c:set var="hideSearchBar" value="${true}" scope="request"/>
<jsp:include page="components/topBar.jsp"/>


<body>
<div class="small-container">
    <div class="form">
        <c:url value="/login" var="postUrl"/>
        <form
                action="${postUrl}"
                method="post"
                class="z-depth-2"
        >
            <h5 class="center-align">Log in to Cybrary</h5>
            <c:if test="${error!=null}">
                <p class="red-text">There was an error with your login attempt. Please verify your username and password and try again.</p>
            </c:if>
            <div class="input-field">
                <input id="email" type="text" class="validate" name="email" autocomplete="off">
                <label for="email">Email</label>
            </div>
            <div class="input-field">
                <input id="password" type="password" class="validate" name="password">
                <span class="material-icons password-toggle-btn" onclick="togglePasswordVisibility()">visibility_off</span>
                <label for="password">Password</label>
            </div>
            <div>
                <label>
                    <input name="rememberMe" type="checkbox"/>
                    <span>Remember Me</span>
                </label>
            </div>
            <div class="input-field center-align submit-btn">
                <button class="btn waves-effect waves-light white-text" type="submit" name="action">
                    Log in
                </button>
            </div>
            <p class="center-align">Don't have an account yet? <a href="${pageContext.request.contextPath}/signup">Sign up</a></p>
        </form>
    </div>
</div>
</body>

<script src="<c:url value="/js/togglePasswordView.js"/>"></script>
</html>
