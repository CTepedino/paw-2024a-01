<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<head>
    <title><c:out value="${book.title}"/></title>
    <link href="<c:url value="/css/bookInfo.css"/>" rel="stylesheet"/>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons"/>
    <link href="<c:url value="/css/paginationControls.css"/>" rel="stylesheet"/>
    <link href="<c:url value="/css/starRating.css"/>" rel="stylesheet"/>
    <link href="<c:url value="/css/questions.css"/>" rel="stylesheet"/>
    <link href="<c:url value="/css/profile.css"/>" rel="stylesheet"/>
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/images/cybrary.png"/>"/>
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
                        <c:if test="${book.salesCategory eq 'BEST_SELLER'}">
                            <div class="sales-category bestseller">
                                <p><spring:message code="book.bestseller"/></p>
                            </div>
                        </c:if>
                        <c:if test="${book.salesCategory eq 'POPULAR'}">
                            <div class="sales-category popular">
                                <p><spring:message code="book.popular"/></p>
                            </div>
                        </c:if>
                        <a href="<c:url value="/profile/${book.writer.userId}"/>">
                            <h6>
                                <spring:message var="author" code="book.bookInfo.author" arguments="${book.writer.firstName},${book.writer.lastName}"/>
                                <c:out value="${author}"/>
                            </h6>
                        </a>
                        <c:if test="${not empty reviews.page or loggedUserReview ne null}">
                            <script src="<c:url value="/js/starRating.js"/>"></script>
                            <script>
                                new FixedStarRating(${avgRating});
                            </script>
                            <c:if test="${loggedUserReview ne null}">
                                <span>(${reviews.totalSize+1})</span>
                            </c:if>
                            <c:if test="${loggedUserReview eq null}">
                                <span>(${reviews.totalSize})</span>
                            </c:if>
                        </c:if>
                        <c:if test="${book.deal eq null}">
                            <h5 class="price"><c:out value="${book.formattedPrice}"/></h5>
                        </c:if>
                        <c:if test="${book.deal ne null}">
                            <h6 class="price"><span class="strikethrough"><c:out value="${book.formattedPrice}"/></span><span class="percentage"><c:out value="${book.percentage}"/></span></h6>
                            <h5 class="price"><c:out value="${book.deal.formattedPrice}"/></h5>
                        </c:if>
                    </div>
                    <div class="col s5">
                        <c:if test="${(not existsOrder or not isLoggedIn) and not book.paused and not isAuthor}">
                            <a href="<c:url value="/sendBuyInfo/${book.bookId}"/>" class="waves-effect waves-light btn action-button">
                                <strong><spring:message code="book.bookInfo.buyBook"/></strong>
                            </a>
                        </c:if>
                        <c:if test="${not existsOrder and isLoggedIn and not isAuthor}">
                            <form action="<c:url value="/wishlist/${book.bookId}"/>" method="post">
                                <button type="submit" class="waves-effect waves-light btn action-button">
                                    <c:if test="${not isWishlisted}">
                                        <strong><spring:message code="wishlist.add"/></strong>
                                    </c:if>
                                    <c:if test="${isWishlisted}">
                                        <strong><spring:message code="wishlist.remove"/></strong>
                                    </c:if>
                                </button>
                            </form>
                        </c:if>
                        <c:if test="${isAuthor}">
                            <a href="<c:url value="/book/edit/${book.bookId}"/>">
                                <button type="submit" class="waves-effect waves-light btn action-button">
                                    <strong><spring:message code="book.editBook"/></strong>
                                </button>
                            </a>

                            <c:if test="${book.deal eq null}">
                                <a href="<c:url value="/book/${book.bookId}/deal"/>">
                                    <button type="submit" class="waves-effect waves-light btn action-button">
                                        <strong><spring:message code="book.bookInfo.createDeal"/></strong>
                                    </button>
                                </a>
                            </c:if>
                            <c:if test="${book.deal ne null}">
                                <a class="waves-effect waves-light btn modal-trigger action-button" href="#dealModal">
                                    <strong><spring:message code="book.bookInfo.viewDeal"/></strong>
                                </a>
                            </c:if>

                        </c:if>
                        <c:if test="${ownsBook or (isAuthor and not book.paused)}">
                            <a href="<c:url value="/book/file/${book.bookId}"/>" target="_blank">
                                <button type="submit" class="waves-effect waves-light btn btn action-button">
                                    <strong><spring:message code="orders.purchases.action.COMPLETED"/></strong>
                                </button>
                            </a>
                        </c:if>
                        <c:if test="${not ownsBook and existsOrder and not isAuthor and isLoggedIn}">
                            <p><spring:message code="book.bookInfo.alreadyBought"/></p>
                            <a href="<c:url value="/purchases"/>">
                                <button type="submit" class="waves-effect waves-light btn btn action-button">
                                    <strong><spring:message code="book.bookInfo.gotToPurchases"/></strong>
                                </button>
                            </a>
                        </c:if>
                        <c:if test="${ownsBook and loggedUserReview eq null}">
                            <a class="waves-effect waves-light btn modal-trigger action-button" href="#reviewModal">
                                <strong><spring:message code="book.bookInfo.WriteReview"/></strong>
                            </a>
                        </c:if>
                        <c:if test="${ownsBook and loggedUserReview ne null}">
                            <a class="waves-effect waves-light btn modal-trigger action-button" href="#reviewModal">
                                <strong><spring:message code="book.bookInfo.EditReview"/></strong>
                            </a>
                        </c:if>
                        <c:if test="${ownsBook and order ne null}">
                            <c:url value="/recommendBook/${order.orderId}/bookInfo" var="recommendBookUrl"/>
                            <form id="recommendBookForm" action="${recommendBookUrl}" method="post" class="recommendation">
                                <label for="recommended-${order.orderId}">
                                    <input type="checkbox" id="recommended-${order.orderId}" name="recommended" ${order.isPublic ? 'checked' : ''}/>
                                    <span><spring:message code="orders.purchases.recommendBook"/></span>
                                </label>
                            </form>
                            <script>
                                document.getElementById('recommended-${order.orderId}').addEventListener('change', function() {
                                    document.getElementById('recommendBookForm').submit();
                                });
                            </script>
                        </c:if>
                        <c:if test="${not existsOrder and book.paused and not isAuthor}">
                            <p class="red-text"><spring:message code="book.bookInfo.paused"/></p>
                        </c:if>
                        <c:if test="${isAuthor and book.paused}">
                            <c:if test="${book.writer.cbu eq null}">
                                <a href="<c:url value="/profile"/>">
                                    <button type="submit" class="waves-effect waves-light btn btn action-button">
                                        <strong><spring:message code="book.bookInfo.gotToProfile"/></strong>
                                    </button>
                                </a>
                                <p class="red-text"><spring:message code="book.bookInfo.noCBU"/></p>
                            </c:if>
                            <c:if test="${book.writer.cbu ne null}">
                                <p class="red-text"><spring:message code="book.bookInfo.noBook"/></p>
                            </c:if>
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


            <div class="divider"></div>
            <c:if test="${not isAuthor and not existsOrder}">
                <c:url value="/book/${bookId}/question" var="questionPostUrl"/>
                <form:form
                        modelAttribute="questionForm"
                        action="${questionPostUrl}"
                        method="post"
                        enctype="multipart/form-data"
                >
                    <div class="row">
                        <div class="input-field col s9">
                            <i class="material-icons prefix">question_answer</i>
                            <form:label path="question"><spring:message code="book.bookInfo.questions.label"/></form:label>
                            <form:textarea path="question" type="text" class="materialize-textarea"/>
                            <form:errors path="question" cssClass="red-text" element="p"/>
                        </div>
                        <div class="col s3 send-button">
                            <button class="btn waves-effect waves-light" type="submit" name="action">
                                <spring:message code="book.bookInfo.questions.send"/>
                                <i class="material-icons right">send</i>
                            </button>
                        </div>
                    </div>
                </form:form>
            </c:if>


        <c:url var="bookInfoUrl" value="/book/${bookId}"/>

        <c:if test="${not isAuthor and isLoggedIn}">
            <div class="row table-top">
                <a href="${bookInfoUrl}/reviews">
                    <c:if test="${tab eq 'reviews'}">
                        <div class="col s4 table-title active">
                            <c:if test="${loggedUserReview ne null}">
                                <p class="text-active" style="width: 100%"><spring:message code="book.bookInfo.tab.reviews"/> (${reviews.totalSize+1})</p>
                            </c:if>
                            <c:if test="${loggedUserReview eq null}">
                                <p class="text-active" style="width: 100%"><spring:message code="book.bookInfo.tab.reviews"/> (${reviews.totalSize})</p>
                            </c:if>
                        </div>
                    </c:if>
                    <c:if test="${tab ne 'reviews'}">
                        <div class="col s4 table-title">
                            <c:if test="${loggedUserReview ne null}">
                                <p class="text-not-active" style="width: 100%"><spring:message code="book.bookInfo.tab.reviews"/> (${reviews.totalSize+1})</p>
                            </c:if>
                            <c:if test="${loggedUserReview eq null}">
                                <p class="text-not-active" style="width: 100%"><spring:message code="book.bookInfo.tab.reviews"/> (${reviews.totalSize})</p>
                            </c:if>
                        </div>
                    </c:if>
                </a>
                <a href="${bookInfoUrl}/questions">
                    <c:if test="${tab eq 'questions'}">
                        <div class="col s4 table-title active">
                            <p class="text-active" style="width: 100%"><spring:message code="book.bookInfo.tab.questions"/> (${questions.totalSize})</p>
                        </div>
                    </c:if>
                    <c:if test="${tab ne 'questions'}">
                        <div class="col s4 table-title">
                            <p class="text-not-active" style="width: 100%"><spring:message code="book.bookInfo.tab.questions"/> (${questions.totalSize})</p>
                        </div>
                    </c:if>
                </a>

                <a href="${bookInfoUrl}/myQuestions">
                    <c:if test="${tab eq 'myQuestions'}">
                        <div class="col s4 table-title active">
                            <p class="text-active" style="width: 100%"><spring:message code="book.bookInfo.tab.myQuestions"/> (${myQuestions.totalSize})</p>
                        </div>
                    </c:if>
                    <c:if test="${tab ne 'myQuestions'}">
                        <div class="col s4 table-title">
                            <p class="text-not-active" style="width: 100%"><spring:message code="book.bookInfo.tab.myQuestions"/> (${myQuestions.totalSize})</p>
                        </div>
                    </c:if>
                </a>
            </div>
        </c:if>

        <c:if test="${isAuthor or not isLoggedIn}">
            <div class="row table-top">
                <a href="${bookInfoUrl}/reviews">
                    <c:if test="${tab eq 'reviews'}">
                        <div class="col s6 table-title active">
                            <p class="text-active" style="width: 100%"><spring:message code="book.bookInfo.tab.reviews"/> (${reviews.totalSize})</p>
                        </div>
                    </c:if>
                    <c:if test="${tab ne 'reviews'}">
                        <div class="col s6 table-title">
                            <p class="text-not-active" style="width: 100%"><spring:message code="book.bookInfo.tab.reviews"/> (${reviews.totalSize})</p>
                        </div>
                    </c:if>
                </a>
                <a href="${bookInfoUrl}/questions">
                    <c:if test="${tab eq 'questions'}">
                        <div class="col s6 table-title active">
                            <p class="text-active" style="width: 100%"><spring:message code="book.bookInfo.tab.questions"/> (${questions.totalSize})</p>
                        </div>
                    </c:if>
                    <c:if test="${tab ne 'questions'}">
                        <div class="col s6 table-title">
                            <p class="text-not-active" style="width: 100%"><spring:message code="book.bookInfo.tab.questions"/> (${questions.totalSize})</p>
                        </div>
                    </c:if>
                </a>
            </div>
        </c:if>

        <c:if test="${tab eq 'myQuestions'}">
            <c:set var="myQuestions" value="${myQuestions}" scope="request"/>
            <c:set var="myQuestionsPage" value="${myQuestionsPage}" scope="request"/>
            <%@include file="components/myQuestionsTab.jsp"%>
        </c:if>

        <c:if test="${tab eq 'questions'}">
            <c:set var="questions" value="${questions}" scope="request"/>
            <c:set var="isAuthor" value="${isAuthor}" scope="request"/>
            <c:set var="answerForm" value="${answerForm}" scope="request"/>
            <%@include file="components/questionsTab.jsp"%>
        </c:if>

        <c:if test="${tab eq 'reviews'}">
            <c:set var="reviews" value="${reviews}" scope="request"/>
            <c:set var="loggedUserReview" value="${loggedUserReview}" scope="request"/>
            <c:set var="reviewSortForm" value="${reviewSortForm}" scope="request"/>
            <c:set var="reviewOrders" value="${reviewOrders}" scope="request"/>
            <c:set var="book" value="${book}" scope="request"/>
            <c:set var="reviewForm" value="${reviewForm}" scope="request"/>
            <%@include file="components/reviewTab.jsp"%>
        </c:if>

        <c:if test="${pageCount gt 1}">
        <script src="<c:url value="/js/paginationControls.js"/>"></script>
        <script>
            const paginationButtonsQuestions = new PaginationButtons(${pageCount}, Math.min(10,${pageCount}), ${pageNumber}, false);
            paginationButtonsQuestions.render();
            paginationButtonsQuestions.onChange(e => {
                window.location.href = "<c:url value="?page="/>" + e.target.value;
            });
        </script>
        </c:if>

    <div id="buyModal" class="modal">
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

        <c:url value="/book/${bookId}/${book.deal.dealId}/endDeal" var="endDealUrl"/>
        <div id="dealModal" class="modal">
            <div class="modal-content">
                <h4><spring:message code="book.bookInfo.deal"/></h4>
                <p><spring:message code="book.addDeal.previousPrice"/> <c:out value="${book.formattedPrice}"/></p>
                <p><spring:message code="book.addDeal.newPrice"/> <c:out value="${book.deal.formattedPrice}"/></p>
                <p><spring:message code="book.bookInfo.deal.startDate"/> <c:out value="${book.deal.startDate}"/></p>
                <p><spring:message code="book.bookInfo.deal.endDate"/> <c:out value="${book.deal.endDate}"/></p>
            </div>
            <div class="modal-footer">
                <div class="footer-aligner">
                    <button class="btn modal-close close-btn" ><strong><spring:message code="close"/></strong></button>
                    <form id="end-deal"  action="${endDealUrl}" method="post">
                        <button class="waves-light btn accept-button-modal" type="submit"><strong><spring:message code="book.bookInfo.endDeal"/></strong></button>
                    </form>
                </div>
            </div>
        </div>



    <script>
        document.addEventListener('DOMContentLoaded', function() {
            var elems = document.querySelectorAll('.modal');
            var instances = M.Modal.init(elems, {});
        });

        document.addEventListener('DOMContentLoaded', function () {
            var elems = document.querySelectorAll('select');
            var instances = M.FormSelect.init(elems);
        });
    </script>
    <script src="<c:url value="/js/selectableStarRating.js"/>"></script>
    <script>
        initializeRating(${reviewForm.rating});
    </script>
</body>

</html>
