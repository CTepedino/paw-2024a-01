<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<spring:eval expression="@environment.getProperty('base.url')" var="baseUrl"/>
<html>
<head>
    <title><spring:message code="profile.analytics"/></title>
    <script src="https://kit.fontawesome.com/0f001c5d7a.js" crossorigin="anonymous"></script>
    <link href="<c:url value="/css/dashboard.css"/>" rel="stylesheet"/>
    <link href="<c:url value="/css/salesView.css"/>" rel="stylesheet"/>

    <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/images/cybrary.png"/>"/>
</head>
<%@include file="components/topBar.jsp" %>
<body>
    <div class="container dashboard">
        <h2 class="page-title"><spring:message code="profile.analytics"/></h2>
        <div class="row">
            <div class="col s2 m5">
                <div class="card-panel">
                    <span class="white-text">
                        <spring:message code="profile.analytics.totalOrders"/>
                        <br/>
                        <c:out value="${totalOrders}"/>
                    </span>
                </div>
            </div>
            <div class="col s2 m5">
                <div class="card-panel">
                    <span class="white-text">
                        <spring:message code="profile.analytics.totalRevenue"/>
                        <br/>
                        <c:out value="${totalRevenue}"/>
                    </span>
                </div>
            </div>
        </div>
        <div class="row table-top">
            <div class="col s6 table-title"> <spring:message code="profile.analytics.book"/> </div>
            <div class="col s3 table-title"> <spring:message code="profile.analytics.totalOrders"/></div>
            <div class="col s3 table-title"> <spring:message code="profile.analytics.totalRevenue"/> </div>
        </div>
        <ul class="collection">
            <c:forEach var="book" items="${books}">
                <li class="collection-item">
                    <div class="row purchased-book">
                        <div class="col s2">
                            <a class="card-image waves-effect waves-block waves-light" href="${pageContext.request.contextPath}/book/${book.book.bookId}">
                                <img
                                        class="book_cover"
                                        src="<c:url value="${baseUrl}/cover/${book.book.bookId}"/>"
                                        alt="<spring:message code="bookInfoCard.cover"/>"
                                />
                            </a>
                        </div>
                        <div class="col s4 purchase-info">
                            <a class="book-title" href="${pageContext.request.contextPath}/book/${book.book.bookId}"><c:out value="${book.book.title}"/></a>
                            <p class="price"> <c:out value="${book.book.formattedPrice}"/></p>
                        </div>
                        <div class="col s3 purchase-info">
                            <p><c:out value="${book.totalOrders}"/></p>
                        </div>
                        <div class="col s3 purchase-info">
                            <p><c:out value="${book.formattedTotalSales}"/></p>
                        </div>
                    </div>
                </li>
            </c:forEach>
        </ul>
    </div>
</body>
</html>
