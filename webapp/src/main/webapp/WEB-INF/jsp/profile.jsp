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

<body>

<c:set value="${true}" scope="request" var="hideSearchBar"/>
<%@include file="components/topBar.jsp" %>

<div class="header__wrapper">
    <div class="right__col">
        <header></header>
        <div class="left__col">
            <div class="img__container">
                <img src="<c:url value="/profilePicture/${user.userId}"/>" alt="user profile picture"/>
        </div>
            <h2><c:out value="${user.firstName}"/>  <c:out value="${user.lastName}"/> </h2>
            <p> <c:out value="${user.email}"/> </p>
            <div class="edit-profile">
                <a href="${pageContext.request.contextPath}/editProfile" class="waves-effect waves-light btn profile-btn">
                    Edit Profile
                </a>
                <a href="${pageContext.request.contextPath}/changePassword" class="waves-effect waves-light btn profile-btn">
                <spring:message code="session.changePassword"/>
                </a>
            </div>
            <nav>
                <c:url var="profileUrl" value="/profile/${userId}"/>
                <ul>
                    <li><a href="${profileUrl}/boughtBooks">Bought Books</a></li>
                    <li><a href="${profileUrl}/publications">Publications</a></li>
                </ul>
            </nav>

        </div>
    </div>
</div>


</body>
</html>
