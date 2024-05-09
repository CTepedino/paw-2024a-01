<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:eval expression="@environment.getProperty('base.url')" var="baseUrl"/>

<!DOCTYPE html>
<html>
<header>
    <link href="${pageContext.request.contextPath}/css/smallBookCard.css" rel="stylesheet"/>
</header>
<body>

<div class="col s3">
    <a href="${pageContext.request.contextPath}/book/${cardBook.bookId}">
        <div class="card">
            <div class="card-image waves-effect waves-block waves-light">
                <img
                        src="<c:url value="${baseUrl}/cover/${cardBook.bookId}"/>"
                        class="activator book_cover"
                        alt="<spring:message code="bookInfoCard.cover"/>"
                />
            </div>
            <div class="card-info" href="${pageContext.request.contextPath}/book/${cardBook.bookId}">
                <div class="container content">
                    <div class="card-content" >
                        <span class="card-title grey-text text-darken-4"><c:out value="${cardBook.title}"/></span>
                        <h5>
                            <c:out value="${cardBook.formattedPrice}"/>
                        </h5>
                    </div>
                </div>
            </div>
        </div>
    </a>
</div>

</body>
</html>
