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
</head>

<%@include file="components/materializeComponent.jsp"%>

<body>
<jsp:include page="components/topBar2.0.jsp">
    <jsp:param name="hasWriterRole" value="${hasWriterRole}" />
</jsp:include>

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
            <h2> My Purchases</h2>
            <spring:message code="orders.purchases.header"/>
        </div>
<%--        <div class="user--info">--%>
<%--            <div class="search--box">--%>
<%--                <a href="${pageContext.request.contextPath}/" > <i class="fa-solid fa-house"></i></a>--%>
<%--            </div>--%>
<%--        </div>--%>

    </div>

    <div class="tabular-wrapper">
        <h3 class="main--title">
            <spring:message code="orders.history"/>
        </h3>
        <div class="table-container">
            <table class="my-table">
                <thead>
                <tr>
                    <th><spring:message code="orders.writer"/></th>
                    <th><spring:message code="orders.email"/></th>
                    <th><spring:message code="orders.title"/></th>
                    <th><spring:message code="orders.price"/></th>
                    <th><spring:message code="orders.status"/></th>
                    <th><spring:message code="orders.action"/></th>
                </tr>
                </thead>
                <tbody>
<%--                <c:forEach var="order" items="${order}">--%>
<%--                    <c:set var="order" value="${order}" scope="request"/>--%>
<%--                    --%>
<%--                </c:forEach>--%>
                <c:forEach var="order" items="${orders}">
                <tr class="my-tr">
                        <td class="my-td"><c:out value="${order.writer.firstName} ${order.writer.lastName}"/></td>
                        <td class="my-td"><c:out value="${order.writer.email}"/></td>
                        <td class="my-td"><c:out value="${order.book.title}"/></td>
                        <td class="my-td"><c:out value="${order.book.price}"/></td>
                        <td class="my-td"><c:out value="${order.orderStatus.displayString}"/></td>
                        <c:url value="/advanceOrder" var="advanceOrderUrl">
                            <c:param name="bookId" value="${order.book.bookId}"/>
                            <c:param name="buyerId" value="${order.buyer.userId}"/>
                            <c:param name="writerId" value="${order.writer.userId}"/>
                            <c:param name="from" value="purchases"/>
                        </c:url>

                        <c:if test="${order.orderStatus.readerCanAdvance}">
                        <td class="my-td"><form action="${advanceOrderUrl}" method="post">
                            <button
                                type="submit"
                            >Advance</button>
                        </form></td>
                        </c:if>
                        <c:if test="${!order.orderStatus.readerCanAdvance}">
                            <td class="my-td"><form action="${advanceOrderUrl}" method="post">
                                <button type="submit" disabled>Advance</button>
                            </form></td>
                        </c:if>
                </tr>
                </c:forEach>

                </tbody>
                <tfoot>

                </tfoot>
            </table>
        </div>
    </div>

</div>
</div>
</body>
</html>
