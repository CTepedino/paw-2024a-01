<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <link href="<c:url value="/css/homeCard.css"/>" rel="stylesheet"/>
</head>
<body>
<div class="col s2">
    <a href="<c:url value="/book/${book.bookId}"/>">
        <div class="card-home small-book-card z-depth-2">
            <c:if test="${book.salesCategory eq 'BEST_SELLER'}">
                <div class="card-badge bestseller"><spring:message code="book.bestseller"/></div>
            </c:if>
            <c:if test="${book.salesCategory eq 'POPULAR'}">
                <div class="card-badge popular"><spring:message code="book.popular"/></div>
            </c:if>
            <div class="card-image-home waves-effect waves-block waves-light">
                <img
                        src="<c:url value="/cover/${book.bookId}"/>"
                        class="activator"
                        alt="<spring:message code="bookInfoCard.cover"/>"
                />
            </div>
            <div class="card-info-home" href="<c:url value="/book/${book.bookId}"/>">
                <div class="container content-home">
                    <div class="card-content-home" >
                        <h6 class="card-title-home grey-text text-darken-4"><c:out value="${book.title}"/></h6>
                        <p class="info-home">
                            <spring:message var="author" code="bookInfoCard.by" arguments="${book.writer.firstName},${book.writer.lastName}"/>
                            <c:out value="${author}"/>
                        </p>
                        <p class="price-number-home">
                            <c:out value="${book.formattedPrice}"/>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </a>
</div>

</body>
</html>
