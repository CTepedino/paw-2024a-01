<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
    <link href="<c:url value="/css/smallsearchInfoCard.css"/>" rel="stylesheet"/>
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/images/cybrary.png"/>"/>
</head>
<body>

<div class="col s6">
    <a href="<c:url value="/book/${book.bookId}"/>">
        <div class="card">
            <c:if test="${book.salesCategory eq 'BEST_SELLER'}">
                <div class="card-badge bestseller"><spring:message code="book.bestseller"/></div>
            </c:if>
            <c:if test="${book.salesCategory eq 'POPULAR'}">
                <div class="card-badge popular"><spring:message code="book.popular"/></div>
            </c:if>
            <div class="card-image waves-effect waves-block waves-light">
                <img
                        src="<c:url value="/cover/${book.bookId}"/>"
                        class="activator book-cover"
                        alt="<spring:message code="bookInfoCard.cover"/>"
                />
            </div>
            <div class="card-info">
                <div class="container content">
                    <div class="card-content" >
                        <span class="card-title grey-text text-darken-4"><c:out value="${book.title}"/></span>
                        <p class="info">
                            <spring:message var="author" code="bookInfoCard.by" arguments="${book.writer.firstName},${book.writer.lastName}"/>
                            <c:out value="${author}"/>
                        </p>
                        <br/>
                        <p class="info">
                            <spring:message var="genre" code="book.genre.${book.genre}"/>
                            <c:out value="${genre}"/>
                        </p>
                        <c:if test="${rating ne 0}">
                            <br/>
                            <i class="material-icons black-icons">star</i>
                            <p class="info">
                                <c:out value="${rating}"/>
                            </p>
                        </c:if>
                        <br/>
                        <i class="material-icons black-icons">face</i>
                        <p class="info">
                            +<c:out value="${book.suggestedAge}"/>
                        </p>
                        <br/>
                        <i class="material-icons black-icons">description</i>
                        <p class="info">
                            <c:out value="${book.pageCount}"/>
                        </p>
                        <br/>
                        <i class="material-icons black-icons">calendar_month</i>
                        <p class="info">
                            <c:out value="${book.publishDate.year}"/>
                        </p>
                    </div>
                    <h5 class="price-number">
                        <c:out value="${book.formattedPrice}"/>
                    </h5>
                </div>
            </div>
        </div>
    </a>
</div>

</body>
</html>

