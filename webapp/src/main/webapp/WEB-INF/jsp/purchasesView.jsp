<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<head>
    <title><spring:message code="orders.purchases.title"/></title>
    <script src="https://kit.fontawesome.com/0f001c5d7a.js" crossorigin="anonymous"></script>
    <link href="<c:url value="/css/purchasesView.css"/>" rel="stylesheet"/>
    <link href="<c:url value="/css/paginationControls.css"/>" rel="stylesheet"/>

    <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/images/cybrary.png"/>"/>
</head>

<%@include file="components/topBar.jsp" %>

<body>
<c:url value="/purchases" var="purchasesUrl"/>
<div class="container purchases">
    <h2 class="page-title"><spring:message code="orders.purchases.header"/></h2>

    <form:form modelAttribute="orderSearchForm"
               action="${purchasesUrl}"
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
                        <form:option value="${status}"><spring:message code="orders.purchases.status.option.${status}"/></form:option>
                    </c:forEach>
                </form:select>
            </div>
        </div>
        <input type="submit" hidden />
        <input name="page" id="page" style="display: none"/>
    </form:form>


    <c:if test="${empty orders.page}">
        <div class="centerer">
            <h5><spring:message code="orders.purchases.empty"/></h5>
        </div>
    </c:if>
    <c:if test="${not empty orders.page}">
        <div class="row table-top">
            <div class="col s2 table-title"> <spring:message code="orders.table.cover"/> </div>
            <div class="col s3 table-title"> <spring:message code="orders.table.book"/> </div>
            <div class="col s2 table-title"> <spring:message code="orders.table.lastUpdate"/></div>
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
                            <a href="<c:url value="/profile/${order.writer.userId}"/>">
                                <p><spring:message var="author" code="bookInfoCard.by" arguments="${order.writer.firstName},${order.writer.lastName}"/><c:out value="${author}"/></p>
                            </a>
                            <p class="price"><c:out value="${order.book.formattedPrice}"/></p>
                        </div>

                    <div class="col s2 purchase-info">

                        <p><c:out value="${order.getFormattedDate(pageContext.request.locale)}"/></p>
                    </div>
                    <div class="col s3 purchase-info">
                        <c:if test="${order.orderStatus eq 'REJECTED_PAYMENT'}">
                            <p class="red-text rejection"><spring:message code="orders.purchases.status.${order.orderStatus}"/></p>
                            <p class="rejection"><c:out value="${order.writer.cbu}"/></p>
                            <a class="btn modal-trigger btn-small rejection" href="#reason"><strong><spring:message code="orders.purchases.status.reason_rejected"/></strong></a>
                            <div id="reason" class="modal">
                                <div class="modal-content">
                                    <h4><spring:message code="orders.purchases.status.reason_rejected"/></h4>
                                    <p><c:out value="${order.rejectedReason}"/></p>
                                </div>
                                <div class="modal-footer">
                                    <div class="footer-aligner">
                                        <button class="btn modal-close close-btn" ><strong><spring:message code="close"/></strong></button>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${order.orderStatus ne 'REJECTED_PAYMENT'}">
                            <p><spring:message code="orders.purchases.status.${order.orderStatus}"/></p>
                        </c:if>
                        <c:if test="${order.orderStatus eq 'WAITING_PAYMENT'}">
                            <c:out value="${order.writer.cbu}"/>
                        </c:if>

                    </div>
                    <div class="col s2 purchase-info">
                        <c:url value="/advanceOrder/${order.orderId}/purchases" var="advanceOrderUrl"/>

                        <c:if test="${order.orderStatus eq 'WAITING_PAYMENT' or order.orderStatus eq 'REJECTED_PAYMENT'}">
                            <form:form id="advanceOrder-${order.orderId}-sendFile"  action="${advanceOrderUrl}" method="post" modelAttribute="updateOrderForm" enctype="multipart/form-data">
                                <form:label path="receipt" for="files-${order.orderId}" cssClass="btn label-select">
                                    <spring:message code="orders.purchases.chooseFile"/>
                                </form:label>
                                <form:input type="file" id="files-${order.orderId}" path="receipt" accept="application/pdf, image/*" style="display:none;"/>
                                <form:errors path="receipt"/>
                                <button class="waves-light btn payment" type="submit">
                                    <strong><spring:message code="orders.purchases.action.${order.orderStatus}"/></strong>
                                </button>
                            </form:form>
                        </c:if>

                        <script>
                            document.addEventListener("DOMContentLoaded", function() {
                                if (document.querySelector("#files-${order.orderId}") != null) {
                                    document.querySelector("#files-${order.orderId}").onchange = function () {
                                        const fileName = this.files[0]?.name;
                                        const label = document.querySelector("label[for=files-${order.orderId}]");
                                        label.innerText = fileName ?? "<spring:message code="orders.purchases.chooseFile"/>";
                                    };
                                }
                            });
                        </script>

                        <c:if test="${order.orderStatus eq 'COMPLETED'}">
                            <a href="<c:url value="/book/file/${order.book.bookId}"/>" target="_blank" style="width: 100%">
                                <button class="waves-light btn"><strong><spring:message code="orders.purchases.action.${order.orderStatus}"/></strong></button>
                            </a>
                            <c:url value="/recommendBook/${order.orderId}/purchases" var="recommendBookUrl"/>

                            <form id="recommendBookForm-${order.orderId}" action="${recommendBookUrl}" method="post" class="recommendation">
                                <label for="recommended-${order.orderId}">
                                    <input type="checkbox" id="recommended-${order.orderId}" name="recommended" ${order.isPublic ? 'checked' : ''}/>
                                    <span><spring:message code="orders.purchases.recommendBook"/></span>
                                </label>
                            </form>
                            <script>
                                document.getElementById('recommended-${order.orderId}').addEventListener('change', function() {
                                    document.getElementById('recommendBookForm-${order.orderId}').submit();
                                });
                            </script>
                        </c:if>

                        <c:if test="${!order.orderStatus.readerCanAdvance and order.orderStatus ne 'COMPLETED'}">
                            <p><spring:message code="orders.purchases.action.${order.orderStatus}"/><i class="material-icons left">hourglass_top</i>
                            </p>
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