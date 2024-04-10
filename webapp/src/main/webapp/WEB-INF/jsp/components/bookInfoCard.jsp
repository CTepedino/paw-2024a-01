<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:eval expression="@environment.getProperty('base.url')" var="baseUrl"/>
<html>
<header>
    <link href="${pageContext.request.contextPath}/css/bookInfoCard.css" rel="stylesheet"/>
</header>
<body>

<div class="card book_card" >
    <a href="${pageContext.request.contextPath}/${book.bookId}">
        <div class="card-image waves-effect waves-block waves-light">
            <img src="<c:url  value="${baseUrl}/image/${book.imageId}"/>" class="activator book_cover" alt="Book cover">
        </div>
    </a>
    <a class="card-info" href="${pageContext.request.contextPath}/${book.bookId}">
        <div class="card-content" >
            <span class="card-title grey-text text-darken-4">${book.title}</span>
            <p>By ${book.writerName} ${book.writerLastName}</p>
            <p>${book.genre.displayName}</p>
            <p>Suggested age: + ${book.suggestedAge}</p>
        </div>
        <div class="card-action">
            <p>Price: $${book.price}</p>
        </div>
    </a>
</div>

</body>
</html>
