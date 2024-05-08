<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:eval expression="@environment.getProperty('base.url')" var="baseUrl"/>

<!DOCTYPE html>
<html>
<header>
    <link href="${pageContext.request.contextPath}/css/bookInfoCard.css" rel="stylesheet"/>
</header>
<body>

<div class="col s6">
    <a href="${pageContext.request.contextPath}/book/${book.bookId}">
        <div class="card">
            <div class="card-image waves-effect waves-block waves-light">
                <img
                    src="<c:url value="${baseUrl}/cover/${book.bookId}"/>"
                    class="activator book_cover"
                    alt="<spring:message code="bookInfoCard.cover"/>"
                />
            </div>
            <div class="card-info" href="${pageContext.request.contextPath}/book/${book.bookId}">
                <div class="container content">
                    <div class="card-content" >
                        <span class="card-title grey-text text-darken-4"><c:out value="${book.title}"/></span>
                        <p class="info">
                            <spring:message var="author" code="bookInfoCard.by" arguments="${book.writer.firstName},${book.writer.lastName}"/>
                            <c:out value="${author}"/>
                        </p>
                        <p class="info">
                            <spring:message var="genre" code="book.genre.${book.genre}"/>
                            <c:out value="${genre}"/>
                        </p>
                        <p class="info">
                            <spring:message var="age" code="bookInfoCard.suggestedAge" arguments="${book.suggestedAge}"/>
                            <c:out value="${age}"/>
                        </p>
                    </div>
                    <h5 class="price-number">
                        <spring:message var="price" code="bookInfoCard.price" arguments="${book.price}"/>
                        <c:out value="${price}"/>
                    </h5>
                </div>
            </div>
        </div>
    </a>
</div>

</body>
</html>
