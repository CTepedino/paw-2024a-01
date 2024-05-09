<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<spring:eval expression="@environment.getProperty('base.url')" var="baseUrl"/>

<!DOCTYPE html>
<html>
<head>
    <title><spring:message code="orders.sales.title"/></title>
    <script src="https://kit.fontawesome.com/0f001c5d7a.js" crossorigin="anonymous"></script>
    <link href="<c:url value="/css/salesView.css"/>" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/sidebarPlus.css" rel="stylesheet"/>
    <link href="<c:url value="/css/paginationControls.css"/>" rel="stylesheet"/>
</head>

<jsp:include page="components/topBar.jsp">
    <jsp:param name="hasWriterRole" value="${isWriter}" />
</jsp:include>

<body>
<c:url value="/sales" var="salesUrl"/>
<div class="container sales">
    <h2 class="page-title"><spring:message code="orders.sales.header"/></h2>

    <form:form modelAttribute="orderSearchForm"
               action="${salesUrl}"
               method="get"
               id="orders">
        <div class="row">
            <div class="col s6">
                <form:input path="title"/>
            </div>
            <div class="input-field col s6">
                <form:select path="orderStatus" onchange="this.form.submit()">
                    <form:option value=""><spring:message code="orders.status"/></form:option>
                    <c:forEach items="${statuses}" var="status">
                        <form:option value="${status}"><spring:message code="orders.sales.status.option.${status}"/></form:option>
                    </c:forEach>
                </form:select>
            </div>
        </div>
    </form:form>

        <div class="row table-top">
            <div class="col s2 table-title"> Cover </div>
            <div class="col s3 table-title"> Title </div>
                <%--        <div class="col s1 table-title"> Price </div>--%>
            <div class="col s2 table-title"> Date </div>
            <div class="col s3 table-title"> Status </div>
            <div class="col s2 table-title"> Actions </div>
        </div>
        <ul class="collection">
            <c:forEach var="order" items="${orders.page}">
                <li class="collection-item">
                    <div class="row purchased-book">
                        <div class="col s2">
                            <a class="card-image waves-effect waves-block waves-light" href="${pageContext.request.contextPath}/book/${order.book.bookId}">
                                <img
                                        class="book_cover"
                                        src="<c:url value="${baseUrl}/cover/${order.book.bookId}"/>"
                                        alt="<spring:message code="bookInfoCard.cover"/>"
                                />
                            </a>
                        </div>
                        <div class="col s3 purchase-info">
                            <a class="book-title" href="${pageContext.request.contextPath}/book/${order.book.bookId}"><c:out value="${order.book.title}"/></a>
                            <p class="price"> <c:out value="${order.book.formattedPrice}"/></p>
                        </div>
                        <div class="col s2 purchase-info">

                            <p><c:out value="${order.date.toLocalDate()}"/></p>
                        </div>
                        <div class="col s3 purchase-info">
                            <p><spring:message code="orders.sales.status.${order.orderStatus}"/></p>
                            <c:if test="${order.orderStatus == 'WAITING_APPROVAL'}">
                                <a href="<c:url value="/receipt/${order.orderId}"/>" target="_blank">
                                    <button class="waves-light btn payment"><spring:message code="orders.sales.status.${order.orderStatus}.receipt"/></button>
                                </a>
                            </c:if>
                        </div>
                        <div class="col s2 purchase-info">

                            <c:url value="/advanceOrder/${order.orderId}/sales" var="advanceOrderUrl"/>

                            <c:if test="${order.orderStatus == 'WAITING_CONTACT'}">
                                <a href="<c:url value="/profile"/>"><button class="waves-light btn"><spring:message code="orders.sales.action.${order.orderStatus}.button"/></button></a>
                            </c:if>

                            <c:if test="${order.orderStatus == 'WAITING_PAYMENT'}">
                                <p><spring:message code="orders.sales.action.${order.orderStatus}"/><i class="material-icons left">hourglass_top</i></p>
                            </c:if>

                            <c:if test="${order.orderStatus == 'WAITING_APPROVAL'}">
                                <form:form action="${advanceOrderUrl}" method="post" modelAttribute="updateOrderForm">
                                    <input type="checkbox" name="approved" value="true" checked style="display: none">
                                    <button class="waves-light btn accept-button" type="submit"><spring:message code="orders.sales.action.${order.orderStatus}.accept"/></button>
                                </form:form>
                                <form:form action="${advanceOrderUrl}" method="post" modelAttribute="updateOrderForm">
                                    <input type="checkbox" name="approved" value="false" checked style="display: none">
                                    <button class="waves-light btn decline-button" type="submit"><spring:message code="orders.sales.action.${order.orderStatus}.decline"/></button>
                                </form:form>
                            </c:if>

                            <c:if test="${order.orderStatus == 'COMPLETED'}">
                                <p><spring:message code="orders.sales.action.${order.orderStatus}"/></p>
                            </c:if>
                        </div>
                    </div>
                </li>
            </c:forEach>
        </ul>

        <c:if test="${orders.pageCount > 1}">
            <input type="number" id="page" name="page" value="${orders.pageNumber}" style="display: none"/>
            <script src="<c:url value="/js/paginationControls.js"/>"></script>
            <script>
                const paginationButtons = new PaginationButtons(${orders.pageCount}, Math.min(10, ${orders.pageCount}), ${orders.pageNumber}, true);
                paginationButtons.render();
                paginationButtons.onChange(e => {
                    document.getElementById('page').value = e.target.value;
                    document.getElementById("orders").submit();
                })
            </script>
        </c:if>


</div>


<script type="module">
    // Initialize Materialize components
    document.addEventListener('DOMContentLoaded', function() {
        var elems = document.querySelectorAll('.sidenav');
        var instances = M.Sidenav.init(elems);
    });

    document.addEventListener('DOMContentLoaded', function () {
        var elems = document.querySelectorAll('select');
        var instances = M.FormSelect.init(elems);
    });
</script>


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
<%--            <spring:message code="orders.sales.header"/>--%>
<%--        </div>--%>
<%--        <div class="user--info">--%>
<%--            <div class="search--box">--%>
<%--                <a href="${pageContext.request.contextPath}/" > <i class="fa-solid fa-house"></i></a>--%>
<%--            </div>--%>
<%--        </div>--%>

<%--    </div>--%>
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
<%--    <div class="tabular-wrapper">--%>
<%--        <h3 class="main--title">--%>
<%--            Order History--%>
<%--        </h3>--%>
<%--        <div class="table-container">--%>
<%--            <table>--%>
<%--                <thead>--%>
<%--                <tr>--%>
<%--                    &lt;%&ndash;<th>Date</th>&ndash;%&gt;--%>
<%--                    <th><spring:message code="orders.buyersEmail"/></th>--%>
<%--                    <th><spring:message code="orders.title"/></th>--%>
<%--                    <th><spring:message code="orders.price"/></th>--%>
<%--                    <th><spring:message code="orders.status"/></th>--%>
<%--                    <th><spring:message code="orders.action"/></th>--%>
<%--                </tr>--%>
<%--                </thead>--%>
<%--                <tbody>--%>
<%--                <c:if test="${orders != null}">--%>
<%--                    <c:forEach var="order" items="${orders}">--%>
<%--                    <tr>--%>
<%--                        &lt;%&ndash;<td> 2024-05-01 </td>&ndash;%&gt;--%>
<%--                        <td><c:out value="${order.buyer.email}"/></td>--%>
<%--                        <td><c:out value="${order.book.title}"/></td>--%>
<%--                        <td><c:out value="${order.book.price}"/></td>--%>
<%--                        <td><c:out value="${order.orderStatus.displayString}"/></td>--%>
<%--                        <c:url value="/advanceOrder" var="advanceOrderUrl">--%>
<%--                            <c:param name="bookId" value="${order.book.bookId}"/>--%>
<%--                            <c:param name="buyerId" value="${order.buyer.userId}"/>--%>
<%--                            <c:param name="writerId" value="${order.writer.userId}"/>--%>
<%--                            <c:param name="from" value="sales"/>--%>
<%--                        </c:url>--%>
<%--                            <c:if test="${order.orderStatus.writerCanAdvance}">--%>
<%--                                <td><form action="${advanceOrderUrl}" method="post">--%>
<%--                                    <button--%>
<%--                                            type="submit"--%>
<%--                                    >Advance</button>--%>
<%--                                </form></td>--%>
<%--                            </c:if>--%>
<%--                            <c:if test="${!order.orderStatus.writerCanAdvance}">--%>
<%--                                <td><form action="${advanceOrderUrl}" method="post">--%>
<%--                                    <button type="submit" disabled>Advance</button>--%>
<%--                                </form></td>--%>
<%--                            </c:if>--%>
<%--                    </tr>--%>

<%--                    </c:forEach>--%>
<%--                </c:if>--%>

<%--                </tbody>--%>
<%--                <tfoot>--%>

<%--                </tfoot>--%>
<%--            </table>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--</div>--%>
</body>
</html>

