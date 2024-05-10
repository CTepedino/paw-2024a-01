<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:eval expression="@environment.getProperty('base.url')" var="baseUrl"/>

<!DOCTYPE html>
<html>
<head>
    <title><c:out value="${book.title}"/></title>
    <link href="${pageContext.request.contextPath}/css/bookInfo.css" rel="stylesheet"/>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons"/>
</head>

<jsp:include page="components/topBar.jsp">
    <jsp:param name="hasWriterRole" value="${isWriter}" />
</jsp:include>

<body>
    <div class="book-container z-depth-2" style="margin: 30px;padding: 20px;">
        <div class="row">
            <div class="col s5">
                <img
                        class="book_cover"
                        src="<c:url value="${baseUrl}/cover/${book.bookId}"/>"
                        alt="<spring:message code="bookInfoCard.cover"/>"
                />
            </div>
            <div class="col s7">
                <div class="row">
                    <h2 class="col s8">
                        <c:out value="${book.title}"/>
                    </h2>
                    <div class="col s4 star-rating">
                        <script src="<c:url value="/js/starRating.js"/>"></script>
                        <script>
                            new FixedStarRating(${avgRating});
                        </script>
                    </div>
                </div>
                <div class="row">
                    <div class="col s8">
                        <h5>
                            <spring:message var="author" code="book.bookInfo.author" arguments="${book.writer.firstName},${book.writer.lastName}"/>
                            <c:out value="${author}"/>
                        </h5>
                    </div>
                    <div class="col s4">
                        <c:if test="${(not isAuthor) and (not ownsBook) and isLoggedIn}">
                            <c:url var="buyUrl" value="/sendBuyInfo">
                                <c:param name="bookId" value="${book.bookId}" />
                            </c:url>
                            <form action="${buyUrl}" method="post">
                                <button type="submit" class="waves-effect waves-light btn white-text">
                                    <spring:message code="book.bookInfo.buyBook"/>
                                </button>
                            </form>
                        </c:if>
                        <c:if test="${isAuthor}">
                            <a href="<c:url value="/book/edit/${book.bookId}"/>">
                                <button type="submit" class="waves-effect waves-light btn white-text">
                                    <spring:message code="book.editBook"/>
                                </button>
                            </a>
                        </c:if>
                        <c:if test="${ownsBook or isAuthor}">
                            <a href="<c:url value="/book/file/${book.bookId}"/>" target="_blank">
                                <button type="submit" class="waves-effect waves-light btn white-text">
                                    <spring:message code="orders.purchases.action.COMPLETED"/>
                                </button>
                            </a>
                        </c:if>
                    </div>
                </div>
                <h5><c:out value="${book.formattedPrice}"/></h5>
                <table>
                    <tbody>
                    <tr>
                        <td><spring:message code="book.bookInfo.recommendedAge"/></td>
                        <td><c:out value="${book.suggestedAge}"/></td>
                    </tr>
                    <tr>
                        <td><spring:message code="book.bookInfo.genre"/></td>
                        <td><spring:message code="book.genre.${book.genre}"/></td>
                    </tr>
                    <tr>
                        <td><spring:message code="book.bookInfo.pageCount"/></td>
                        <td><c:out value="${book.pageCount}"/></td>
                    </tr>
                    <tr>
                        <td><spring:message code="book.bookInfo.publishDate"/></td>
                        <td><c:out value="${book.publishDate}"/></td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="col s12">
                <p><c:out value="${book.description}"/></p>
            </div>
            <div class="col s12">
                <h6><spring:message code="book.bookInfo.preview"/></h6>
                <object
                        type="application/pdf"
                        data="<c:url value="${baseUrl}/preview/${book.bookId}" />"
                        width="100%"
                        height="700"
                >
                </object>
            </div>
            <c:if test="${not empty recommendations}">
                <div class="col s12">
                    <h5>
                        <spring:message code="book.bookInfo.recommendations"/>
                    </h5>
                </div>
            </c:if>
            <div class="col s12">
                <c:forEach var="recommendedBook" items="${recommendations}" varStatus="loop">
                    <c:if test="${loop.index < 4}">
                        <c:set var="cardBook" value="${recommendedBook}" scope="request"/>
                        <c:set var="myBooks" value="${false}" scope="request"/>
                        <%@include file="components/smallBookCard.jsp"%>
                    </c:if>
                </c:forEach>
            </div>
            <c:if test="${ownsBook && loggedUserReview eq null}">
                <a href="<c:url value="/book/${book.bookId}/review/${loggedUser.userId}"/>">
                    <button class="waves-effect waves-light btn white-text">
                        <spring:message code="review.writeReview"/>
                    </button>
                </a>
            </c:if>
            <c:if test="${loggedUserReview ne null}">
                <h5><spring:message code="review.yourReview"/></h5>
                <a href="<c:url value="/book/${book.bookId}/review/${loggedUser.userId}"/>">
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
                <table>
                    <tbody>
                        <c:forEach items="${reviews.page}" var="review">
                            <th class="col s4">
                                <script>
                                    new FixedStarRating(${review.rating});

                                </script><br/>
                                <spring:message code="bookInfoCard.by" var="reviewer" arguments="${review.reviewer.firstName},${review.reviewer.lastName}"/>
                                <c:out value="${reviewer}"/><br/>
                            </th>
                            <th class="col s8">
                                <c:out value="${review.review}"/>
                            </th>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>
        </div>
    </div>
</body>
</html>
