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
                        <p class="info-home title-home"><c:out value="${book.title}"/></p>
                        <p class="info-home author-home">
                            <spring:message var="author" code="bookInfoCard.by" arguments="${book.writer.firstName},${book.writer.lastName}"/>
                            <c:out value="${author}"/>
                        </p>
                        <c:if test="${book.deal eq null}">
                            <h5 class="price-number-home">
                                <c:out value="${book.formattedPrice}"/>
                            </h5>
                        </c:if>
                        <c:if test="${book.deal ne null}">
                            <h6 class="price-number-crossed-home"><span class="strikethrough"><c:out value="${book.formattedPrice}"/></span><span class="percentage"><c:out value="${book.percentage}"/></span></h6>
                            <h5 class="price-number-new-home"><c:out value="${book.deal.formattedPrice}"/></h5>
                        </c:if>

                    </div>
                </div>
            </div>
        </div>
    </a>
</div>

</body>
</html>
