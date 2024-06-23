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
    <link href="<c:url value="/css/paginationControls.css"/>" rel="stylesheet"/>

</head>
<%@include file="components/topBar.jsp" %>
<body>
    <div class="container dashboard">
        <h2 class="page-title"><spring:message code="profile.analytics"/></h2>
        <div class="row">
            <div class="col">
                <c:if test="${user.writerCategory eq 'DEFAULT'}">
                    <span>+ ${bronzeMin - totalOrders} <spring:message code="profile.analytics.toBronze"/></span>
                </c:if>
                <c:if test="${user.writerCategory eq 'BRONZE'}">
                    <span class="sales-category bronze"><spring:message code="bronze"/></span>
                    <span>+ ${silverMin - totalOrders} <spring:message code="profile.analytics.toSilver"/></span>
                </c:if>
                <c:if test="${user.writerCategory eq 'SILVER'}">
                    <span class="sales-category silver"><spring:message code="silver"/></span>
                    <span>+ ${goldMin - totalOrders} <spring:message code="profile.analytics.toGold"/></span>
                </c:if>
                <c:if test="${user.writerCategory eq 'GOLD'}">
                    <p class="sales-category gold"><spring:message code="gold"/></p>
                </c:if>
            </div>
        </div>
        <div class="row">
            <div class="col s3 ">
                <div class="card-panel">
                    <span class="white-text">
                        <spring:message code="profile.analytics.totalOrders"/>
                        <br/>
                        <c:out value="${totalOrders}"/>
                    </span>
                </div>
            </div>
            <div class="col s3 ">
                <div class="card-panel">
                    <span class="white-text">
                        <spring:message code="profile.analytics.totalRevenue"/>
                        <br/>
                        <c:out value="${totalRevenue}"/>
                    </span>
                </div>
            </div>
            <div class="col s3 ">
                <div class="card-panel">
                    <span class="white-text">
                        <spring:message code="profile.analytics.ordersThisMonth"/>
                        <br/>
                        <c:out value="${totalOrdersThisMonth}"/>
                        <c:if test="${ordersChange ne '0'}">
                            <span class="color-yellow"><c:out value="${ordersChange}"/> </span>
                        </c:if>
                    </span>
                </div>
            </div>
            <div class="col s3 ">
                <div class="card-panel">
                    <span class="white-text">
                        <spring:message code="profile.analytics.revenueThisMonth"/>
                        <br/>
                        <c:out value="${totalRevenueThisMonth}"/>
                        <span class="color-yellow"><c:out value="${revenueChange}"/> </span>
                    </span>
                </div>
            </div>
        </div>
        <c:url value="/analytics" var="analyticsUrl"/>
        <form:form modelAttribute="analyticsForm"
                   action="${analyticsUrl}"
                   method="get"
                   id="books">
            <div class="row">
                <label path="byMonth" id="byMonth">
                    <input type="checkbox" path="byMonth" name="byMonth" onchange="this.form.submit()" ${showMonths ? 'checked' : ''}/>
                    <span><spring:message code="profile.analytics.showByMonth"/></span>
                </label>
            </div>
            <c:if test="${showMonths}">
                <div class="row">
                    <div class="input-field col s6">
                        <form:label path="year" cssClass="active">
                            <spring:message code="profile.analytics.select.year"/>
                        </form:label><br>
                        <form:select path="year" onchange="this.form.submit()">
                            <c:forEach items="${years}" var="year">
                                <form:option value="${year}">${year}</form:option>
                            </c:forEach>
                        </form:select>
                    </div>
                    <div class="input-field col s6">
                        <form:label path="month" cssClass="active">
                            <spring:message code="profile.analytics.select.month"/>
                        </form:label><br>
                        <form:select path="month" onchange="this.form.submit()">
                            <c:forEach items="${months}" var="month">
                                <form:option value="${month}"><spring:message code="month.${month}"/></form:option>
                            </c:forEach>
                        </form:select>
                    </div>
                </div>
                <div class="row">
                    <div class="col s3 ">
                        <div class="card-panel">
                            <span class="white-text">
                                <spring:message code="profile.analytics.ordersThatMonth"/>
                                <br/>
                                <c:out value="${totalOrdersThatMonth}"/>
                            </span>
                        </div>
                    </div>
                    <div class="col s3 ">
                        <div class="card-panel">
                            <span class="white-text">
                                <spring:message code="profile.analytics.revenueThatMonth"/>
                                <br/>
                                <c:out value="${totalRevenueThatMonth}"/>
                            </span>
                        </div>
                    </div>
                </div>
            </c:if>
            <input type="submit" hidden />
            <input name="page" id="page" style="display: none"/>
        </form:form>

        <div class="row table-top">
            <div class="col s6 table-title"> <spring:message code="profile.analytics.book"/> </div>
            <div class="col s3 table-title"> <spring:message code="profile.analytics.totalOrders"/></div>
            <div class="col s3 table-title"> <spring:message code="profile.analytics.totalRevenue"/> </div>
        </div>
        <c:if test="${empty books.page}">
            <div class="centerer no-books">
                <h5><spring:message code="profile.analytics.noBooks"/></h5>
            </div>
        </c:if>
        <c:if test="${not empty books.page}">
            <ul class="collection">
                <c:forEach var="book" items="${books.page}">
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
                                <c:if test="${!showMonths}">
                                    <c:if test="${book.book.salesCategory eq 'DEFAULT'}">
                                        <span>+ ${popularMin - book.totalOrders} <spring:message code="profile.analytics.toPopular"/></span>
                                    </c:if>
                                    <c:if test="${book.book.salesCategory eq 'POPULAR'}">
                                        <p class="sales-category popular"><spring:message code="book.popular"/></p>
                                        <span>+ ${bestSellerMin - book.totalOrders} <spring:message code="profile.analytics.toBestSeller"/></span>
                                    </c:if>
                                    <c:if test="${book.book.salesCategory eq 'BEST_SELLER'}">
                                        <p class="sales-category bestseller"><spring:message code="book.bestseller"/></p>
                                    </c:if>
                                </c:if>
                            </div>
                            <div class="col s3 purchase-info">
                                <p><c:out value="${book.formattedTotalSales}"/></p>
                            </div>
                        </div>
                    </li>
                </c:forEach>
            </ul>
        </c:if>

        <c:if test="${books.pageCount > 1}">
            <script src="<c:url value="/js/paginationControls.js"/>"></script>
            <script>
                const paginationButtons = new PaginationButtons(${books.pageCount}, Math.min(10, ${books.pageCount}), ${books.pageNumber}, true);
                paginationButtons.render();
                paginationButtons.onChange(e => {
                    document.getElementById('page').value = e.target.value;
                    document.getElementById("books").submit();
                })
            </script>
        </c:if>
    </div>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        var elems = document.querySelectorAll('select');
        var instances = M.FormSelect.init(elems);
    });
    document.getElementById('byMonth').addEventListener('change', function() {
        document.getElementById('byMonth').submit();
    });


</script>

</body>
</html>