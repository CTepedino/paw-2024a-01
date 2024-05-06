<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:eval expression="@environment.getProperty('base.url')" var="baseUrl"/>

<!DOCTYPE html>
<html>
<head>
    <title><spring:message code="orders.purchases.title"/></title>
    <script src="https://kit.fontawesome.com/0f001c5d7a.js" crossorigin="anonymous"></script>
    <link href="${pageContext.request.contextPath}/css/sidebarPlus.css" rel="stylesheet"/>
    <link href="<c:url value="/css/purchasesView.css"/>" rel="stylesheet"/>
</head>

<jsp:include page="components/topBar.jsp">
    <jsp:param name="hasWriterRole" value="${hasWriterRole}" />
</jsp:include>

<body>
<div class="container purchases">
    <h2 class="page-title"><spring:message code="orders.purchases.header"/></h2>

    <div class="row table-top">
        <div class="col s1 table-title"> Cover </div>
        <div class="col s4 table-title"> Title </div>
        <div class="col s1 table-title"> Price </div>
        <div class="col s4 table-title"> Status </div>
        <div class="col s2 table-title"> Actions </div>
    </div>
    <ul class="collection">
        <c:forEach var="order" items="${orders}">
        <li class="collection-item">
            <div class="row purchased-book">
                <div class="col s1">
                    <div class="card-image waves-effect waves-block waves-light">
                        <img
                            class="book_cover"
                            src="<c:url value="${baseUrl}/image/${order.book.coverId}"/>"
                            alt="<spring:message code="bookInfoCard.cover"/>"
                        />
                    </div>
                </div>
                <div class="col s4 purchase-info">
                    <p><c:out value="${order.book.title}"/></p>
                    <p>by <c:out value="${order.writer.firstName} ${order.writer.lastName}"/></p>
                </div>
                <div class="col s1 purchase-info">
                    <p>$ <c:out value="${order.book.price}"/></p>
                </div>
                <div class="col s4 purchase-info">
                    <p><spring:message code="orders.purchases.status.${order.orderStatus}"/></p>
                </div>
                <div class="col s2 purchase-info">
                    <c:url value="/advanceOrder" var="advanceOrderUrl">
                        <c:param name="bookId" value="${order.book.bookId}"/>
                        <c:param name="buyerId" value="${order.buyer.userId}"/>
                        <c:param name="writerId" value="${order.writer.userId}"/>
                        <c:param name="from" value="purchases"/>
                    </c:url>
                    <c:if test="${order.orderStatus == 'WAITING_PAYMENT'}">
                        <form action="${advanceOrderUrl}" method="post">
                            <button class="waves-light btn payment" type="submit"><spring:message code="orders.purchases.action.${order.orderStatus}"/></button>
                        </form>
                    </c:if>
                    <c:if test="${order.orderStatus == 'COMPLETED'}">
                        <button class="waves-light btn"><spring:message code="orders.purchases.action.${order.orderStatus}"/></button>
                    </c:if>
                    <c:if test="${!order.orderStatus.readerCanAdvance}">
                        <p><spring:message code="orders.purchases.action.${order.orderStatus}"/></p>
                    </c:if>
                </div>
            </div>
        </li>
        </c:forEach>
    </ul>
</div>
<%--<div class="main--content">--%>
<%--    <nav>--%>
<%--        <div class="nav-wrapper">--%>
<%--            <a href="${pageContext.request.contextPath}/" class="brand-logo"> <img class="logo" src="${pageContext.request.contextPath}/images/cybrary_3.png"></a>--%>
<%--        </div>--%>
<%--        <style>--%>
<%--            <%@include file="/css/topBarStyle.css" %>--%>
<%--        </style>--%>
<%--        <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>--%>
<%--    </nav>--%>


<%--    <div class="header-wrapper">--%>
<%--        <div class="header--title">--%>
<%--            <spring:message code="orders.purchases.header"/>--%>
<%--        </div>--%>
<%--        <div class="user--info">--%>
<%--            <div class="search--box">--%>
<%--                <a href="${pageContext.request.contextPath}/" > <i class="fa-solid fa-house"></i></a>--%>
<%--            </div>--%>
<%--        </div>--%>

<%--    </div>--%>

<%--    <div class="tabular-wrapper">--%>
<%--        <h3 class="main--title">--%>
<%--            <spring:message code="orders.history"/>--%>
<%--        </h3>--%>
<%--        <div class="table-container">--%>
<%--            <table>--%>
<%--                <thead>--%>
<%--                <tr>--%>
<%--                    <th><spring:message code="orders.writer"/></th>--%>
<%--                    <th><spring:message code="orders.email"/></th>--%>
<%--                    <th><spring:message code="orders.title"/></th>--%>
<%--                    <th><spring:message code="orders.price"/></th>--%>
<%--                    <th><spring:message code="orders.status"/></th>--%>
<%--                    <th><spring:message code="orders.action"/></th>--%>
<%--                </tr>--%>
<%--                </thead>--%>
<%--                <tbody>--%>
<%--&lt;%&ndash;                <c:forEach var="order" items="${order}">&ndash;%&gt;--%>
<%--&lt;%&ndash;                    <c:set var="order" value="${order}" scope="request"/>&ndash;%&gt;--%>
<%--&lt;%&ndash;                    &ndash;%&gt;--%>
<%--&lt;%&ndash;                </c:forEach>&ndash;%&gt;--%>
<%--                <c:forEach var="order" items="${orders}">--%>
<%--                <tr>--%>
<%--                        <td><c:out value="${order.writer.firstName} ${order.writer.lastName}"/></td>--%>
<%--                        <td><c:out value="${order.writer.email}"/></td>--%>
<%--                        <td><c:out value="${order.book.title}"/></td>--%>
<%--                        <td><c:out value="${order.book.price}"/></td>--%>
<%--                        <td><c:out value="${order.orderStatus.displayString}"/></td>--%>
<%--                        <c:url value="/advanceOrder" var="advanceOrderUrl">--%>
<%--                            <c:param name="bookId" value="${order.book.bookId}"/>--%>
<%--                            <c:param name="buyerId" value="${order.buyer.userId}"/>--%>
<%--                            <c:param name="writerId" value="${order.writer.userId}"/>--%>
<%--                            <c:param name="from" value="purchases"/>--%>
<%--                        </c:url>--%>

<%--                        <c:if test="${order.orderStatus.readerCanAdvance}">--%>
<%--                        <td><form action="${advanceOrderUrl}" method="post">--%>
<%--                            <button--%>
<%--                                type="submit"--%>
<%--                            >Advance</button>--%>
<%--                        </form></td>--%>
<%--                        </c:if>--%>
<%--                        <c:if test="${!order.orderStatus.readerCanAdvance}">--%>
<%--                            <td><form action="${advanceOrderUrl}" method="post">--%>
<%--                                <button type="submit" disabled>Advance</button>--%>
<%--                            </form></td>--%>
<%--                        </c:if>--%>
<%--                </tr>--%>
<%--                </c:forEach>--%>

<%--                </tbody>--%>
<%--                <tfoot>--%>

<%--                </tfoot>--%>
<%--            </table>--%>
<%--        </div>--%>
<%--    </div>--%>

<%--</div>--%>
</body>
</html>
