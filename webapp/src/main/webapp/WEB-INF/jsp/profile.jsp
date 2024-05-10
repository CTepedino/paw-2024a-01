<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><spring:message code="user.profile.title"/></title>
    <link href="${pageContext.request.contextPath}/css/profile.css" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
</head>

<jsp:include page="components/topBar.jsp">
    <jsp:param name="hasWriterRole" value="${isWriter}" />
</jsp:include>

<body>
<div class="medium-container">
    <div class="card-panel center-align">
        <i class="material-icons large">account_circle</i>
        <div class="name">
            <span><c:out value="${loggedUser.firstName}"/></span>
            <span><c:out value="${loggedUser.lastName}"/></span>
        </div>
        <div class="email">
            <span><c:out value="${loggedUser.email}"/></span>
        </div>
        <div class="edit-profile">
            <a href="${pageContext.request.contextPath}/changePassword" class="waves-effect waves-light btn">
                <spring:message code="session.changePassword"/>
            </a>
        </div>
    </div>
</div>
</body>
</html>
