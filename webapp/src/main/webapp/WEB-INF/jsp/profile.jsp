<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><spring:message code="user.profile.title"/></title>
    <link href="<c:url value="/css/profile.css"/>" rel="stylesheet"/>

    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
</head>

<body>

<c:set value="${true}" scope="request" var="hideSearchBar"/>
<%@include file="components/topBar.jsp" %>

<div class="container profile center">
    <img class="profile-img" src="<c:url value="/profilePicture/${user.userId}"/>" alt="user profile picture"/>
    <h2 class="writer-name"><c:out value="${user.firstName}"/>  <c:out value="${user.lastName}"/> </h2>
    <p class="writer-info"> <c:out value="${user.email}"/> </p>
    <c:if test="${ownsProfile}">
        <c:if test="${isWriter}">
            <p class="writer-info">
                <spring:message var="cbuMsj" code="user.profile.cbu" arguments="${user.cbu}"/>
                <c:out value="${cbuMsj}"/>
            </p>
        </c:if>

        <c:if test="${empty user.cbu}">
            <p class="writer-info paused"><spring:message code="user.profile.emptyCBU"/></p>
        </c:if>
        <div class="edit-profile">
            <a href="<c:url value="/editProfile"/>" class="waves-effect waves-light btn profile-btn">
                <spring:message code="user.profile.edit.title"/>
            </a>
            <a href="<c:url value="/changePassword"/>" class="waves-effect waves-light btn profile-btn">
                <spring:message code="session.changePassword"/>
            </a>
        </div>
    </c:if>

    <c:url var="profileUrl" value="/profile/${userId}"/>

    <div class="row table-top">
        <a href="${profileUrl}/publications">
            <c:if test="${publicationSelected}">
                <div class="col s6 table-title active">
                    <p class="text-active" style="width: 100%"><spring:message code="user.profile.publications"/></p>
                </div>
            </c:if>
            <c:if test="${!publicationSelected}">
                <div class="col s6 table-title">
                    <p class="text-not-active" style="width: 100%"><spring:message code="user.profile.publications"/></p>
                </div>
            </c:if>
        </a>
        <a href="${profileUrl}/boughtBooks">
            <c:if test="${boughtBooksSelected}">
                <div class="col s6 table-title active">
                    <p class="text-active" style="width: 100%"><spring:message code="user.profile.boughtBooks"/></p>
                </div>
            </c:if>
            <c:if test="${!boughtBooksSelected}">
                <div class="col s6 table-title">
                    <p class="text-not-active" style="width: 100%"><spring:message code="user.profile.boughtBooks"/></p>
                </div>
            </c:if>
        </a>
    </div>

</div>


</body>
</html>
