<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
    <link href="<c:url value="/css/smallsearchInfoCard.css"/>" rel="stylesheet"/>
</head>
<body>

<div class="col s6">
    <a href="<c:url value="/book/${book.bookId}"/>">
        <div class="card">
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
                        <br/>
                        <p class="info">
                            <spring:message var="age" code="bookInfoCard.suggestedAge" arguments="${book.suggestedAge}"/>
                            <c:out value="${age}"/>
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

