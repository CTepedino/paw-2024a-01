<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
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

    <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/images/cybrary.png"/>"/>
</head>


<%@include file="components/topBar.jsp" %>

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
        <input name="page" id="page" style="display: none"/>
    </form:form>

    <c:if test="${empty orders.page}">
        <div class="centerer">
            <h5><spring:message code="orders.sales.empty"/></h5>
        </div>
    </c:if>
    <c:if test="${not empty orders.page}">
        <div class="row table-top">
            <div class="col s2 table-title"> <spring:message code="orders.table.cover"/> </div>
            <div class="col s3 table-title"> <spring:message code="orders.table.book"/></div>
            <div class="col s2 table-title"> <spring:message code="orders.table.lastUpdate"/> </div>
            <div class="col s3 table-title"> <spring:message code="orders.table.status"/> </div>
            <div class="col s2 table-title"> <spring:message code="orders.table.actions"/> </div>
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
                            <p class="price"> <c:out value="${order.formattedPrice}"/></p>
                            <p><spring:message code="orders.sales.buyer"/> <a href="<c:url value="/profile/${order.buyer.userId}"/>"><c:out value="${order.buyer.firstName}"/> <c:out value="${order.buyer.lastName}"/></a></p>
                        </div>
                        <div class="col s2 purchase-info">

                            <p><c:out value="${order.getFormattedDate(pageContext.request.locale)}"/></p>
                        </div>
                        <div class="col s3 purchase-info">
                            <p><spring:message code="orders.sales.status.${order.orderStatus}"/></p>
                            <c:if test="${order.orderStatus == 'WAITING_APPROVAL' or order.orderStatus == 'COMPLETED'}">
                                <a href="<c:url value="/receipt/${order.orderId}"/>" target="_blank">
                                    <button class="waves-light btn payment"><strong><spring:message code="orders.sales.status.viewReceipt"/></strong></button>
                                </a>
                            </c:if>
                        </div>
                        <div class="col s2 purchase-info">

                            <c:url value="/advanceOrder/${order.orderId}/sales" var="advanceOrderUrl"/>

                            <c:if test="${order.orderStatus == 'WAITING_CONTACT'}">
                                <a href="<c:url value="/profile"/>"><button class="waves-light btn"><strong><spring:message code="orders.sales.action.${order.orderStatus}.button"/></strong></button></a>
                            </c:if>

                            <c:if test="${order.orderStatus == 'WAITING_APPROVAL'}">
                                <a class="waves-light btn decline-button modal-trigger" href="#decline"><spring:message code="orders.sales.action.${order.orderStatus}.decline"/></a>
                                <div id="decline" class="modal">
                                    <form:form id="advanceOrder-${order.orderId}-decline" action="${advanceOrderUrl}" method="post" modelAttribute="updateOrderForm">
                                    <div class="modal-content">
                                        <h4><spring:message code="orders.sales.paymentApproval.title"/></h4>
                                        <p><spring:message code="orders.sales.paymentApproval.decline"/></p>
                                        <div class="input-field">
                                            <form:label path="reason"><spring:message code="orders.sales.action.reason_decline"/></form:label>
                                            <form:textarea path="reason" maxlength="500" class="materialize-textarea"/>
                                        </div>
                                        <form:errors path="reason" cssClass="red-text" element="p"/>
                                    </div>
                                    <div class="modal-footer">
                                        <div class="footer-aligner">
                                            <button class="btn modal-close close-btn">
                                                <strong><spring:message code="cancel"/></strong>
                                            </button>

                                            <input type="checkbox" name="approved" value="false" checked style="display: none">
                                            <button class="waves-light btn decline-button-modal" type="submit"><strong><spring:message code="orders.sales.action.${order.orderStatus}.decline"/></strong></button>
                                        </div>
                                    </div>
                                    </form:form>
                                </div>
                                <a class="waves-light btn accept-button modal-trigger" href="#accept"><spring:message code="orders.sales.action.${order.orderStatus}.accept"/></a>
                                <div id="accept" class="modal">
                                    <div class="modal-content">
                                        <h4><spring:message code="orders.sales.paymentApproval.title"/></h4>
                                        <p><spring:message code="orders.sales.paymentApproval.accept"/></p>
                                    </div>
                                    <div class="modal-footer">
                                        <div class="footer-aligner">
                                            <button class="btn modal-close close-btn" ><strong><spring:message code="cancel"/></strong></button>
                                            <form:form id="advanceOrder-${order.orderId}-accept"  action="${advanceOrderUrl}" method="post" modelAttribute="updateOrderForm">
                                                <input type="checkbox" name="approved" value="true" checked style="display: none">

                                                <button class="waves-light btn accept-button-modal" type="submit"><strong><spring:message code="orders.sales.action.${order.orderStatus}.accept"/></strong></button>
                                            </form:form>
                                        </div>
                                    </div>
                                </div>
                            </c:if>

                            <c:if test="${order.orderStatus == 'COMPLETED'}">
                                <p><spring:message code="orders.sales.action.${order.orderStatus}"/></p>
                            </c:if>
                        </div>
                    </div>
                </li>
            </c:forEach>
        </ul>
    </c:if>

    <c:if test="${orders.pageCount > 1}">
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

    document.addEventListener('DOMContentLoaded', function() {
        var elems = document.querySelectorAll('.modal');
        var instances = M.Modal.init(elems, {});
    });
</script>
</body>
</html>
