<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
    <title><c:out value="${book.title}"/></title>
    <link href="<c:url value="/css/bookInfo.css"/>" rel="stylesheet"/>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons"/>
    <link href="<c:url value="/css/paginationControls.css"/>" rel="stylesheet"/>
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
                        <a href="<c:url value="/profile/${book.writer.userId}"/>">
                            <h6>
                                <spring:message var="author" code="book.bookInfo.author" arguments="${book.writer.firstName},${book.writer.lastName}"/>
                                <c:out value="${author}"/>
                            </h6>
                        </a>
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
                            <a class="waves-effect waves-light btn modal-trigger action-button" href="#modal1"><spring:message code="book.bookInfo.buyBook"/></a>
                            <div id="modal1" class="modal">
                                <div class="modal-content">
                                    <h4><spring:message code="book.bookInfo.buyModalTitle"/></h4>
                                    <p><spring:message code="book.bookInfo.buyModalText"/></p>
                                </div>
                                <div class="modal-footer">
                                    <form action="${buyUrl}" method="post">
                                        <button type="submit" class="waves-effect waves-light btn">
                                            <spring:message code="book.bookInfo.buyBook"/>
                                        </button>
                                    </form>
                                </div>
                            </div>
                            <script>
                                document.addEventListener('DOMContentLoaded', function() {
                                    var elems = document.querySelectorAll('.modal');
                                    var instances = M.Modal.init(elems, {});
                                });
                            </script>
                        </c:if>
                        <c:if test="${isAuthor}">
                            <a href="<c:url value="/book/edit/${book.bookId}"/>">
                                <button type="submit" class="waves-effect waves-light btn action-button">
                                    <strong><spring:message code="book.editBook"/></strong>
                                </button>
                            </a>
                        </c:if>
                        <c:if test="${ownsBook or isAuthor}">
                            <a href="<c:url value="/book/file/${book.bookId}"/>" target="_blank">
                                <button type="submit" class="waves-effect waves-light btn btn action-button">
                                    <strong><spring:message code="orders.purchases.action.COMPLETED"/></strong>
                                </button>
                            </a>
                        </c:if>
                        <c:if test="${ownsBook and loggedUserReview eq null}">
                            <button class="waves-effect waves-light btn btn action-button">
                                <strong><spring:message code="book.bookInfo.WriteReview"/></strong>
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
        <c:if test="${not empty reviews.page or loggedUserReview ne null}">
            <section id="reviews">
                <c:if test="${loggedUserReview ne null}">
                    <div class="divider"></div>
                    <div class="row">
                        <h5 class="col s6"><spring:message code="review.yourReview"/></h5>
                        <div class="review-control col s6">
                            <a href="<c:url value="/book/${book.bookId}/review"/>">
                                <button class="waves-effect waves-light btn white-text">
                                    <strong><spring:message code="review.editReview"/></strong>
                                </button>
                            </a>
                        </div>
                    </div>
                    <c:set var="review" value="${loggedUserReview}" scope="request"/>
                    <%@include file="components/reviewCard.jsp" %>
                </c:if>
                <c:if test="${not empty reviews.page}">
                    <div class="divider"></div>
                    <div class="col s12">
                        <h5>
                            <spring:message code="book.bookInfo.reviews"/><br/>
                        </h5>
                    </div>
                    <c:forEach items="${reviews.page}" var="review">
                        <c:set var="review" value="${review}" scope="request"/>
                        <%@include file="components/reviewCard.jsp" %>
                    </c:forEach>
                    <c:if test="${reviews.pageCount gt 1}">
                        <script src="<c:url value="/js/paginationControls.js"/>"></script>
                        <script>
                            const paginationButtons = new PaginationButtons(${reviews.pageCount}, Math.min(10,${reviews.pageCount}), ${reviews.pageNumber}, false);
                            paginationButtons.render();
                            paginationButtons.onChange(e => {
                                window.location.href = "<c:url value="?reviewPage="/>" + e.target.value;
                            })
                        </script>
                    </c:if>
                </c:if>
            </section>
        </c:if>
    </div>
</body>
</html>
