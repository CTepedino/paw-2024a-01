<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title><spring:message code="orders.sales.title"/></title>
    <script src="https://kit.fontawesome.com/0f001c5d7a.js" crossorigin="anonymous"></script>
    <link href="${pageContext.request.contextPath}/css/sidebarPlus.css" rel="stylesheet"/>
</head>

<jsp:include page="components/topBar.jsp">
    <jsp:param name="hasWriterRole" value="${hasWriterRole}" />
</jsp:include>

<body>
<div class="main--content">
<%--    <nav>--%>
<%--        <div class="nav-wrapper">--%>
<%--            <a href="${pageContext.request.contextPath}/" class="brand-logo"> <img class="logo" src="${pageContext.request.contextPath}/images/cybrary_3.png"></a>--%>
<%--        </div>--%>
<%--        <style>--%>
<%--            <%@include file="/css/topBarStyle.css" %>--%>
<%--        </style>--%>
<%--        <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>--%>
<%--    </nav>--%>


    <div class="header-wrapper">
        <div class="header--title">
            <spring:message code="orders.sales.header"/>
        </div>
        <div class="user--info">
            <div class="search--box">
                <a href="${pageContext.request.contextPath}/" > <i class="fa-solid fa-house"></i></a>
            </div>
        </div>

    </div>
    <%--
    <div class="card--container">
        <h3 class="main--title ">Today's data</h3>
        <div class="card-wrapper">
            <div class="payment-card light-yellow">
                <div class="card--header">
                    <div class="amount">
                                    <span class="title">
                                        Total Sales Revenue
                                    </span>
                        <span class="amount-value">
                                        $500.000
                                    </span>

                    </div>
                    <i class="fas fa-dollar-sign icon"></i>
                </div>
                <span class="card-detail"> More information </span>
            </div>
            <div class="payment-card light-yellow">
                <div class="card--header">
                    <div class="amount">
                                    <span class="title">
                                        Units Sold
                                    </span>
                        <span class="amount-value">
                                        3
                                    </span>

                    </div>
                    <i class="fas fa-list icon"></i>
                </div>
                <span class="card-detail"> More information </span>
            </div>
            <div class="payment-card light-yellow">
                <div class="card--header">
                    <div class="amount">
                                    <span class="title">
                                        Payment proceed
                                    </span>
                        <span class="amount-value">
                                        $150.000
                                    </span>

                    </div>
                    <i class="fas fa-check icon dark-blue"></i>
                </div>
                <span class="card-detail"> More information </span>
            </div>
        </div>
    </div>
    --%>
    <div class="tabular-wrapper">
        <h3 class="main--title">
            Order History
        </h3>
        <div class="table-container">
            <table>
                <thead>
                <tr>
                    <%--<th>Date</th>--%>
                    <th><spring:message code="orders.buyersEmail"/></th>
                    <th><spring:message code="orders.title"/></th>
                    <th><spring:message code="orders.price"/></th>
                    <th><spring:message code="orders.status"/></th>
                    <th><spring:message code="orders.action"/></th>
                </tr>
                </thead>
                <tbody>
                <c:if test="${orders != null}">
                    <c:forEach var="order" items="${orders}">
                    <tr>
                        <%--<td> 2024-05-01 </td>--%>
                        <td><c:out value="${order.buyer.email}"/></td>
                        <td><c:out value="${order.book.title}"/></td>
                        <td><c:out value="${order.book.price}"/></td>
                        <td><c:out value="${order.orderStatus.displayString}"/></td>
                        <c:url value="/advanceOrder" var="advanceOrderUrl">
                            <c:param name="bookId" value="${order.book.bookId}"/>
                            <c:param name="buyerId" value="${order.buyer.userId}"/>
                            <c:param name="writerId" value="${order.writer.userId}"/>
                            <c:param name="from" value="sales"/>
                        </c:url>
                            <c:if test="${order.orderStatus.writerCanAdvance}">
                                <td><form action="${advanceOrderUrl}" method="post">
                                    <button
                                            type="submit"
                                    >Advance</button>
                                </form></td>
                            </c:if>
                            <c:if test="${!order.orderStatus.writerCanAdvance}">
                                <td><form action="${advanceOrderUrl}" method="post">
                                    <button type="submit" disabled>Advance</button>
                                </form></td>
                            </c:if>
                    </tr>

                    </c:forEach>
                </c:if>

                </tbody>
                <tfoot>

                </tfoot>
            </table>
        </div>
    </div>
</div>
</body>
</html>

