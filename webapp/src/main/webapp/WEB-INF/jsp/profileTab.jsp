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

    <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/images/cybrary.png"/>"/>
</head>

<body>

<c:set value="${true}" scope="request" var="hideSearchBar"/>
<%@include file="components/topBar.jsp" %>
<br/>
<div class="container profile center">
    <img class="profile-img" src="<c:url value="/profilePicture/${user.userId}"/>" alt="user profile picture"/>
    <h2 class="writer-name"><c:out value="${user.firstName}"/>  <c:out value="${user.lastName}"/> </h2>
    <p class="writer-info"> <c:out value="${user.email}"/> </p>
    <c:if test="${ownsProfile}">
        <c:if test="${isWriter and not empty user.cbu}">
            <p class="writer-info">
                <spring:message var="cbuMsj" code="user.profile.cbu" arguments="${user.cbu}"/>
                <c:out value="${cbuMsj}"/>
            </p>
        </c:if>

        <c:if test="${isWriter and empty user.cbu}">
            <p class="writer-info paused"><spring:message code="user.profile.emptyCBU"/></p>
        </c:if>
    </c:if>
    <c:if test="${not empty user.description}">
        <div class="container">
            <p class="description">
                <c:out value="${user.description}"/>
            </p>
        </div>
    </c:if>
    <c:if test="${ownsProfile}">
        <div class="edit-profile">
            <a href="<c:url value="/editProfile"/>" class="waves-effect waves-light btn profile-btn">
                <spring:message code="user.profile.edit.title"/>
            </a>
            <a href="<c:url value="/changePassword"/>" class="waves-effect waves-light btn profile-btn">
                <spring:message code="session.changePassword"/>
            </a>
        </div>
    </c:if>

    <c:url var="profileUrl" value="/profile/${user.userId}"/>

    <div class="row table-top">
        <c:if test="${showPublicationsTab}">
        <a href="${profileUrl}/publications">
            <c:if test="${tab eq 'publications'}">
                <div class="col s6 table-title active">
                    <p class="text-active" style="width: 100%"><spring:message code="user.profile.publications"/></p>
                </div>
            </c:if>
            <c:if test="${tab ne 'publications'}">
                <div class="col s6 table-title">
                    <p class="text-not-active" style="width: 100%"><spring:message code="user.profile.publications"/></p>
                </div>
            </c:if>
            <c:if test="${ownsProfile}">
                <div class="col s4 table-title"
            </c:if>
        </a>

        <a href="${profileUrl}/boughtBooks">
            <c:if test="${tab eq 'boughtBooks'}">
                <div class="col s6 table-title active">
                    <p class="text-active" style="width: 100%">
                        <c:if test="${ownsProfile}">
                            <spring:message code="user.profile.boughtBooks"/>
                        </c:if>
                        <c:if test="${!ownsProfile}">
                            <spring:message code="user.profile.recommendedBooks"/>
                        </c:if>
                    </p>
                </div>
            </c:if>
            <c:if test="${tab ne 'boughtBooks'}">
                <div class="col s6 table-title">
                    <p class="text-not-active" style="width: 100%">
                        <c:if test="${ownsProfile}">
                            <spring:message code="user.profile.boughtBooks"/>
                        </c:if>
                        <c:if test="${!ownsProfile}">
                            <spring:message code="user.profile.recommendedBooks"/>
                        </c:if>
                    </p>
                </div>
            </c:if>
        </a>
        </c:if>
        <c:if test="${not showPublicationsTab}">
            <div class="col s12 table-title active">
                <p class="text-active" style="width: 100%">
                    <c:if test="${ownsProfile}">
                        <spring:message code="user.profile.boughtBooks"/>
                    </c:if>
                    <c:if test="${!ownsProfile}">
                        <spring:message code="user.profile.recommendedBooks"/>
                    </c:if>
                </p>
            </div>
        </c:if>
    </div>

</div>
</body>
</html>
