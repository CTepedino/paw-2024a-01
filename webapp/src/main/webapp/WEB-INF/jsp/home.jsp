
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:eval expression="@environment.getProperty('base.url')" var="baseUrl"/>

<html>
<head>
    <title>Cybrary</title>
    <link href="${pageContext.request.contextPath}/css/home.css" rel="stylesheet"/>
</head>
<%@ include file="components/topBar.jsp" %>
<body>
    <div class="explore_back">
        <div class="container">
            <h5 class="explore_books">Find New Books</h5>
            <div>
                <h6 class="steps">1. Select your favorite book</h6>
                <h6 class="steps">2. Share your information with the author</h6>
                <h6 class="steps">3. Arrange delivery</h6>
            </div>
        </div>
    </div>
    <div class="books">
        <div class="container">
        <c:forEach var="book" items="${books}">
            <c:set var="book" value="${book}" scope="request"/>
            <%@include file="components/bookInfoCard.jsp"%>
        </c:forEach>
        </div>
    </div>
</body>
</html>
