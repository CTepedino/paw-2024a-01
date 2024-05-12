<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<header>
    <link href="<c:url value="/css/smallBookCard.css"/>" rel="stylesheet"/>
</header>
<body>

<div class="col s3">
    <a href="<c:url value="/book/${cardBook.bookId}"/>">
        <div class="card small-book-card">
            <div class="card-image waves-effect waves-block waves-light">
                <img
                    src="<c:url value="/cover/${cardBook.bookId}"/>"
                    class="activator"
                    alt="<spring:message code="bookInfoCard.cover"/>"
                />
            </div>
            <div class="card-info" href="<c:url value="/book/${cardBook.bookId}"/>">
                <div class="container content">
                    <div class="card-content" >
                        <span class="card-title grey-text text-darken-4"><c:out value="${cardBook.title}"/></span>
                        <c:if test="${!publications}">
                            <p class="info">
                                <spring:message var="author" code="bookInfoCard.by" arguments="${book.writer.firstName},${book.writer.lastName}"/>
                                <c:out value="${author}"/>
                            </p>
                        </c:if>
                        <c:if test="${ownsProfile and publications}">
                            <p class="info paused">
                                <c:if test="${cardBook.paused}">
                                    <c:if test="${not empty cardBook.writer.cbu}">
                                    <spring:message code="bookInfoCard.pausedFullBook"/>
                                    </c:if>
                                    <c:if test="${empty cardBook.writer.cbu}">
                                        <spring:message code="bookInfoCard.pausedCBU"/>
                                    </c:if>
                                </c:if>
                            </p>
                            <p class="info">
                                <c:if test="${!cardBook.paused}">
                                    <c:out value="${cardBook.publishDate}"/>
                                </c:if>
                            </p>
                        </c:if>
                        <c:if test="${!ownsProfile and publications}">
                            <c:if test="${cardBook.paused}">
                                <p class="info paused"><spring:message code="bookInfoCard.pausedBook"/></p>
                            </c:if>
                            <c:if test="${!cardBook.paused}">
                                <p class="info"><c:out value="${cardBook.publishDate}"/></p>
                            </c:if>
                        </c:if>
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
