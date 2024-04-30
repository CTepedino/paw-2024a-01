<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><spring:message code="session.login"/></title>
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
            <h5 class="center-align"><spring:message code="session.loginTitle"/></h5>
            <c:if test="${error!=null}">
                <p class="red-text"><spring:message code="session.loginError"/></p>
            </c:if>
            <div class="input-field">
                <input id="email" type="text" class="validate" name="email" autocomplete="off">
                <label for="email"><spring:message code="session.email"/></label>
            </div>
            <div class="input-field">
                <input id="password" type="password" class="validate" name="password">
                <span class="material-icons password-toggle-btn" onclick="togglePasswordVisibility()">visibility_off</span>
                <label for="password"><spring:message code="session.password"/></label>
            </div>
            <div>
                <label>
                    <input name="rememberMe" type="checkbox"/>
                    <span><spring:message code="session.rememberMe"/></span>
                </label>
            </div>
            <div class="input-field center-align submit-btn">
                <button class="btn waves-effect waves-light white-text" type="submit" name="action">
                    <spring:message code="session.login"/>
                </button>
            </div>
            <p class="center-align">
                <spring:message code="session.toSignup"/>
                <a href="${pageContext.request.contextPath}/signup">
                    <spring:message code="session.signup"/>
                </a>
            </p>
        </form>
    </div>
</div>
</body>

<script src="<c:url value="/js/togglePasswordView.js"/>"></script>
</html>
