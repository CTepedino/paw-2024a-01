<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:eval expression="@environment.getProperty('base.url')" var="baseUrl"/>
<html>
<header>
    <link href="${pageContext.request.contextPath}/css/bookInfoCard.css" rel="stylesheet"/>
</header>
<body>

<div class="col s6">
    <div class="card">
        <a href="${pageContext.request.contextPath}/${book.bookId}">
            <div class="card-image waves-effect waves-block waves-light">
                <img src="<c:url  value="${baseUrl}/image/${book.imageId}"/>" class="activator book_cover" alt="Book cover">
            </div>
        </a>
        <a class="card-info" href="${pageContext.request.contextPath}/${book.bookId}">
            <div class="container content">
                <div class="card-content" >
                    <span class="card-title grey-text text-darken-4"><c:out value="${book.title}"/></span>
                    <p class="info"><spring:message code="bookInfoCard.by"/> <c:out value="${book.writerName}"/> <c:out value="${book.writerLastName}"/></p>
                    <p class="info"><spring:message code="book.genre.${book.genre}"/></p>
                    <p class="info"><spring:message code="bookInfoCard.suggestedAge"/> <c:out value="${book.suggestedAge}"/></p>
                </div>
                <h5 class="price-number">$<c:out value="${book.price}"/></h5>
            </div>
        </a>
    </div>
</div>

</body>
</html>
