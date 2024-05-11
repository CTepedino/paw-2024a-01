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
            <div class="input-field col s6">
                <form:label path="title" cssClass="active">
                    <spring:message code="book.search.title"/>
                </form:label><br>
                <form:input path="title"/>
            </div>
            <div class="input-field col s6">
                <form:label path="orderStatus" cssClass="active">
                    <spring:message code="orders.status"/>
                </form:label><br>
                <form:select path="orderStatus" onchange="this.form.submit()">
                    <form:option value=""><spring:message code="orders.status.all"/></form:option>
                    <c:forEach items="${statuses}" var="status">
                        <form:option value="${status}"><spring:message code="orders.sales.status.option.${status}"/></form:option>
                    </c:forEach>
                </form:select>
            </div>
        </div>
        <input type="submit" hidden />
    </form:form>

        <div class="row table-top">
            <div class="col s2 table-title"> Cover </div>
            <div class="col s3 table-title"> Book </div>
            <div class="col s2 table-title"> Last update </div>
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
                            <c:if test="${order.orderStatus == 'WAITING_APPROVAL' or order.orderStatus == 'COMPLETED'}">
                                <a href="<c:url value="/receipt/${order.orderId}"/>" target="_blank">
                                    <button class="waves-light btn payment"><spring:message code="orders.sales.status.viewReceipt"/></button>
                                </a>
                            </c:if>
                        </div>
                        <div class="col s2 purchase-info">

                            <c:url value="/advanceOrder/${order.orderId}/sales" var="advanceOrderUrl"/>

                            <c:if test="${order.orderStatus == 'WAITING_CONTACT'}">
                                <a href="<c:url value="/profile"/>"><button class="waves-light btn"><spring:message code="orders.sales.action.${order.orderStatus}.button"/></button></a>
                            </c:if>

                            <c:if test="${(order.orderStatus == 'WAITING_PAYMENT') || (order.orderStatus == 'REJECTED_PAYMENT')}">
                                <p><spring:message code="orders.sales.action.${order.orderStatus}"/><i class="material-icons left">hourglass_top</i></p>
                            </c:if>

                            <c:if test="${order.orderStatus == 'WAITING_APPROVAL'}">
                                <form:form action="${advanceOrderUrl}" method="post" modelAttribute="updateOrderForm">
                                    <input type="checkbox" name="approved" value="false" checked style="display: none">
                                    <button class="waves-light btn decline-button" type="submit"><spring:message code="orders.sales.action.${order.orderStatus}.decline"/></button>
                                </form:form>
                                <form:form action="${advanceOrderUrl}" method="post" modelAttribute="updateOrderForm">
                                    <input type="checkbox" name="approved" value="true" checked style="display: none">
                                    <button class="waves-light btn accept-button" type="submit"><spring:message code="orders.sales.action.${order.orderStatus}.accept"/></button>
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
</body>
</html>

