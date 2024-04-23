
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
<%-- <%@include file="components/sideBar.jsp"%> --%>
<body>
    <div class="explore_back">
    <div class="container">
            <div class="row">
                <div class="col s4">
                    <div class="card-panel center">
                        <img class="home-image" src="${pageContext.request.contextPath}/images/transparent-background-book-24.png">
                        <h6 class="white-text">Find a book</h6>
                    </div>
                </div>
                <div class="col s4">
                    <div class="card-panel center">
                        <img class="home-image" src="${pageContext.request.contextPath}/images/handshake.png">
                        <h6 class="white-text">Contact the writer</h6>
                    </div>
                </div>
                <div class="col s4">
                    <div class="card-panel center">
                        <img class="home-image" src="${pageContext.request.contextPath}/images/book.png">
                        <h6 class="white-text">Get your copy</h6>
                    </div>
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
    </div>

</body>
</html>
