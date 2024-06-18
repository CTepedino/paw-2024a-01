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
    <a href="<c:url value="/book/${book.book.bookId}"/>">
        <div class="card">
            <div class="card-image waves-effect waves-block waves-light">
                <img
                        src="<c:url value="/cover/${book.book.bookId}"/>"
                        class="activator book-cover"
                        alt="<spring:message code="bookInfoCard.cover"/>"
                />
            </div>
            <div class="card-info">
                <div class="container content">
                    <div class="card-content" >
                        <span class="card-title grey-text text-darken-4"><c:out value="${book.book.title}"/></span>
                        <p class="info">
                            <spring:message var="author" code="bookInfoCard.by" arguments="${book.book.writer.firstName},${book.book.writer.lastName}"/>
                            <c:out value="${author}"/>
                        </p>
                        <br/>
                        <p class="info">
                            <spring:message var="genre" code="book.genre.${book.book.genre}"/>
                            <c:out value="${genre}"/>
                        </p>
                        <br/>
                        <i class="material-icons black-icons">face</i>
                        <p class="info">
                            +<c:out value="${book.book.suggestedAge}"/>
                        </p>
                        <br/>
                        <i class="material-icons black-icons">description</i>
                        <p class="info">
                            <c:out value="${book.book.pageCount}"/>
                        </p>
                        <br/>
                        <i class="material-icons black-icons">calendar_month</i>
                        <p class="info">
                            <c:out value="${book.book.publishDate.year}"/>
                        </p>
                    </div>
                    <c:if test="${book.deal eq null}">
                        <h5 class="price-number">
                            <c:out value="${book.book.formattedPrice}"/>
                        </h5>
                    </c:if>
                    <c:if test="${book.deal ne null}">
                        <h6 class="price-number-crossed"><span class="strikethrough"><c:out value="${book.book.formattedPrice}"/></span><span class="percentage"><c:out value="${book.percentage}"/></span></h6>
                        <h5 class="price-number"><c:out value="${book.deal.formattedPrice}"/></h5>
                    </c:if>
                </div>
            </div>
        </div>
    </a>
</div>

</body>
</html>

