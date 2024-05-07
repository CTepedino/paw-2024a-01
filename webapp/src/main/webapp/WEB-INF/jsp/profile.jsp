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

<%@include file="components/materializeComponent.jsp"%>

<body>
<jsp:include page="components/topBar2.0.jsp">
    <jsp:param name="hasWriterRole" value="${hasWriterRole}" />
</jsp:include>
<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">

<div class="header__wrapper">
    <div class="right__col">
        <header></header>
        <div class="left__col">
            <div class="img__container">
                <img src="${pageContext.request.contextPath}/images/user.png"/>
            </div>
            <h2><c:out value="${user.firstName}"/>  <c:out value="${user.lastName}"/> </h2>
            <p> <c:out value="${user.email}"/> </p>
            <div class="cart">
                <p><a href="${pageContext.request.contextPath}/changePassword" >Edit Profile</a></p>
            </div>
            <nav>
                <ul>
                    <li><a href="">Buyed Books</a></li>
                    <li><a href="">My Books</a></li>
                </ul>
            </nav>

            <div class="row">
                <c:forEach var="book" items="${books}">
                    <c:set var="book" value="${book}" scope="request"/>
                    <%@include file="components/bookInfoCard.jsp"%>
                </c:forEach>
            </div>

            <nav>
                <ul>
                    <li><a href="">Suggestions</a></li>
                </ul>
            </nav>


        </div>

<%--        <div class="edit-profile">--%>
<%--            <a href="${pageContext.request.contextPath}/changePassword" class="waves-effect waves-light btn">--%>
<%--                <spring:message code="session.changePassword"/>--%>
<%--            </a>--%>
<%--        </div>--%>

    </div>
</div>

</body>
</html>
