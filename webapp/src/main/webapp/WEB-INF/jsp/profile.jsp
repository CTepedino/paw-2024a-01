<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Sign In</title>
    <link href="${pageContext.request.contextPath}/css/profile.css" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
</head>
<jsp:include page="components/topBar.jsp">
    <jsp:param name="hasWriterRole" value="${hasWriterRole}" />
</jsp:include>
<body>
<div class="medium-container">
    <div class="card-panel center-align">
        <i class="material-icons large">account_circle</i>
        <div class="name">
            <span><c:out value="${user.firstName}"/></span>
            <span>$<c:out value="${user.lastName}"/></span>
        </div>
        <div class="email">
            <span>Email: <c:out value="${user.email}"/></span>
        </div>
        <div class="edit-profile">
            <a href="${pageContext.request.contextPath}/changePassword" class="waves-effect waves-light btn">Change Password</a>
        </div>
    </div>
</div>
</body>
</html>
