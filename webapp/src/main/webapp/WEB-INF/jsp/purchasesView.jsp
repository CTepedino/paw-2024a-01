<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<spring:eval expression="@environment.getProperty('base.url')" var="baseUrl"/>

<!DOCTYPE html>
<html>
<head>
    <title><spring:message code="orders.purchases.title"/></title>
    <script src="https://kit.fontawesome.com/0f001c5d7a.js" crossorigin="anonymous"></script>
    <link href="${pageContext.request.contextPath}/css/sidebarPlus.css" rel="stylesheet"/>
    <link href="<c:url value="/css/purchasesView.css"/>" rel="stylesheet"/>
    <link href="<c:url value="/css/paginationControls.css"/>" rel="stylesheet"/>
</head>

<jsp:include page="components/topBar.jsp">
    <jsp:param name="hasWriterRole" value="${isWriter}" />
</jsp:include>

<body>
<c:url value="/purchases" var="purchasesUrl"/>
<div class="container purchases">
    <h2 class="page-title"><spring:message code="orders.purchases.header"/></h2>

    <form:form modelAttribute="orderSearchForm"
               action="${purchasesUrl}"
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
                        <form:option value="${status}"><spring:message code="orders.purchases.status.option.${status}"/></form:option>
                    </c:forEach>
                </form:select>
            </div>
        </div>


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
                        <p>by <c:out value="${order.writer.firstName} ${order.writer.lastName}"/></p>
                        <p class="price">$ <c:out value="${order.book.price}"/></p>
                    </div>
    <%--                <div class="col s1 purchase-info">--%>
    <%--                    <p>$ <c:out value="${order.book.price}"/></p>--%>
    <%--                </div>--%>
                    <div class="col s2 purchase-info">

                        <p><c:out value="${order.date.toLocalDate()}"/></p>
                    </div>
                    <div class="col s3 purchase-info">
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
                                <button class="waves-light btn payment" type="submit"><spring:message code="orders.purchases.action.${order.orderStatus}" arguments="${order.writer.cbu}"/></button>
                            </form>
                        </c:if>
                        <c:if test="${order.orderStatus == 'COMPLETED'}">
                            <button class="waves-light btn"><spring:message code="orders.purchases.action.${order.orderStatus}"/></button>
                        </c:if>
                        <c:if test="${!order.orderStatus.readerCanAdvance}">
                            <p><spring:message code="orders.purchases.action.${order.orderStatus}"/><i class="material-icons left">hourglass_top</i></p>
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

    </form:form>
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
