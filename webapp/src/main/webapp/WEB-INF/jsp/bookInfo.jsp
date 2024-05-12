<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
    <title><c:out value="${book.title}"/></title>
    <link href="<c:url value="/css/bookInfo.css"/>" rel="stylesheet"/>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons"/>
</head>

<%@include file="components/topBar.jsp" %>

<body>
    <div class="container book-container z-depth-2" >
        <div class="row">
            <div class="col s5">
                <div class="info-image">
                    <img
                        class="book-cover"
                        src="<c:url value="/cover/${book.bookId}"/>"
                        alt="<spring:message code="bookInfoCard.cover"/>"
                    />
                </div>
            </div>
            <div class="col s7">
                <div class="row">
                    <h3><c:out value="${book.title}"/></h3>
                </div>
                <div class="row">
                    <div class="col s7">
                        <h6>
                            <spring:message var="author" code="book.bookInfo.author" arguments="${book.writer.firstName},${book.writer.lastName}"/>
                            <c:out value="${author}"/>
                        </h6>
                        <script src="<c:url value="/js/starRating.js"/>"></script>
                        <script>
                            new FixedStarRating(${avgRating});
                        </script>
                        <h5 class="price"><c:out value="${book.formattedPrice}"/></h5>
                    </div>
                    <div class="col s5">
                        <c:if test="${((not isAuthor and not ownsBook) or not isLoggedIn) and not book.paused}">
                            <c:url var="buyUrl" value="/sendBuyInfo">
                                <c:param name="bookId" value="${book.bookId}" />
                            </c:url>
                            <form action="${buyUrl}" method="post">
                                <button type="submit" class="waves-effect waves-light btn action-button">
                                    <spring:message code="book.bookInfo.buyBook"/>
                                </button>
                            </form>
                        </c:if>
                        <c:if test="${isAuthor}">
                            <a href="<c:url value="/book/edit/${book.bookId}"/>">
                                <button type="submit" class="waves-effect waves-light btn action-button">
                                    <spring:message code="book.editBook"/>
                                </button>
                            </a>
                        </c:if>
                        <c:if test="${ownsBook or isAuthor}">
                            <a href="<c:url value="/book/file/${book.bookId}"/>" target="_blank">
                                <button type="submit" class="waves-effect waves-light btn btn action-button">
                                    <spring:message code="orders.purchases.action.COMPLETED"/>
                                </button>
                            </a>
                        </c:if>
                        <c:if test="${ownsBook}">
                            <button class="waves-effect waves-light btn btn action-button">
                                <spring:message code="book.bookInfo.WriteReview"/>
                            </button>
                        </c:if>
                        <c:if test="${not ownsBook and book.paused}">
                            <p class="red-text"><spring:message code="book.bookInfo.paused"/></p>
                        </c:if>
                    </div>
                </div>
                <div class="row info-boxes">
                    <div class="col s3 center info-box">
                        <i class="material-icons">calendar_month</i>
                        <p><c:out value="${book.publishDate}"/></p>
                    </div>
                    <div class="col s3 center info-box">
                        <i class="material-icons">${book.genre.iconName}</i>
                        <p><spring:message code="book.genre.${book.genre}"/></p>
                    </div>
                    <div class="col s3 center info-box">
                        <i class="material-icons">description</i>
                        <p><c:out value="${book.pageCount}"/></p>
                    </div>
                    <div class="col s3 center">
                        <i class="material-icons">face</i>
                        <p>+<c:out value="${book.suggestedAge}"/></p>
                    </div>
                </div>
            </div>
        </div>
        <div class="container">
            <p class="description"><c:out value="${book.description}"/></p>
        </div>

        <h5><spring:message code="book.bookInfo.preview"/></h5>
        <object
                type="application/pdf"
                data="<c:url value="/preview/${book.bookId}" />"
                width="100%"
                height="700"
        >
        </object>
        <c:if test="${not empty recommendations}">
            <div class="divider"></div>
            <h5><spring:message code="book.bookInfo.recommendations"/></h5>
            <div class="row">
                <c:forEach var="recommendedBook" items="${recommendations}" varStatus="loop">
                    <c:if test="${loop.index < 4}">
                        <c:set var="cardBook" value="${recommendedBook}" scope="request"/>
                        <c:set var="myBooks" value="${false}" scope="request"/>
                        <%@include file="components/smallBookCard.jsp"%>
                    </c:if>
                </c:forEach>
            </div>
        </c:if>
        <c:if test="${not empty reviews or ownsBook}">
            <div class="divider"></div>
            <c:if test="${ownsBook && loggedUserReview eq null}">
                <a href="<c:url value="/book/${book.bookId}/review"/>">
                    <button class="waves-effect waves-light btn white-text">
                        <spring:message code="review.writeReview"/>
                    </button>
                </a>
            </c:if>
            <c:if test="${loggedUserReview ne null}">
                <h5><spring:message code="review.yourReview"/></h5>
                <a href="<c:url value="/book/${book.bookId}/review"/>">
                    <button class="waves-effect waves-light btn white-text">
                        <spring:message code="review.editReview"/>
                    </button>
                </a>
                <th class="col s4">
                    <script>new FixedStarRating(${loggedUserReview.rating});</script><br/>
                    <spring:message code="bookInfoCard.by" var="reviewer" arguments="${loggedUserReview.reviewer.firstName},${loggedUserReview.reviewer.lastName}"/>
                    <c:out value="${reviewer}"/><br/>
                </th>
                <th class="col s8">
                    <c:out value="${loggedUserReview.review}"/>
                </th>
            </c:if>
            <c:if test="${not empty reviews}">
                <div class="col s12">
                    <h5>
                        <spring:message code="book.bookInfo.reviews"/><br/>
                    </h5>
                </div>
                <c:forEach items="${reviews.page}" var="review">
                    <c:set var="review" value="${review}" scope="request"/>
                    <%@include file="components/reviewCard.jsp" %>
                </c:forEach>
            </c:if>
        </c:if>
    </div>
</body>
</html>
