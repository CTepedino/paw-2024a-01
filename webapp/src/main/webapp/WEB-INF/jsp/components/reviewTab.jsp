<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
    <link href="<c:url value="/css/paginationControls.css"/>" rel="stylesheet"/>
    <link href="<c:url value="/css/starRating.css"/>" rel="stylesheet"/>
    <link href="<c:url value="/css/bookInfo.css"/>" rel="stylesheet"/>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons"/>
</head>
<body>
<c:if test="${empty reviews.page and loggedUserReview eq null}">
<div class="container" style="width: 90%!important;">
    <h6><spring:message code="review.noReviews"/></h6>
</c:if>
<c:if test="${not empty reviews.page or loggedUserReview ne null}">
    <section id="reviews">
        <c:if test="${loggedUserReview ne null}">
            <div class="row">
                <h5 class="col s10"><spring:message code="review.yourReview"/></h5>
                <div class="review-control col s2">
                    <a class="waves-effect waves-light btn modal-trigger action-button edit-rev-btn" href="#reviewModal">
                        <strong><spring:message code="review.editReview"/></strong>
                    </a>
                </div>
            </div>
            <c:set var="review" value="${loggedUserReview}" scope="request"/>
            <%@include file="reviewCard.jsp" %>
        </c:if>
        <c:if test="${not empty reviews.page}">
            <div class="divider"></div>
            <div class="row">
                <h5 class="col s6"><spring:message code="book.bookInfo.reviews"/><br/></h5>
                <div class=" col s6">
                    <c:url value="/book/${book.bookId}/reviews" var="reviewOrderUrl"/>
                    <%--@elvariable id="reviewSortForm" type="reviewSortForm"--%>
                    <form:form
                            action="${reviewOrderUrl}"
                            modelAttribute="reviewSortForm"
                            method="get"
                    >
                        <form:select path="orderBy" onchange="this.form.submit()">
                            <c:forEach items="${reviewOrders}" var="order">
                                <form:option value="${order}"><spring:message code="review.${order}"/></form:option>
                            </c:forEach>
                        </form:select>
                    </form:form>
                </div>
            </div>
            <c:forEach items="${reviews.page}" var="review">
                <c:set var="review" value="${review}" scope="request"/>
                <%@include file="reviewCard.jsp" %>
            </c:forEach>
            <%--<c:if test="${reviews.pageCount gt 1}">
                <script src="<c:url value="/js/paginationControls.js"/>"></script>
                <script>
                    const paginationButtons = new PaginationButtons(${reviews.pageCount}, Math.min(10,${reviews.pageCount}), ${reviews.pageNumber}, false);
                    paginationButtons.render();
                    paginationButtons.onChange(e => {
                        window.location.href = "<c:url value="?page="/>" + e.target.value + "#reviews";
                    })
                </script>
            </c:if>--%>
        </c:if>
    </section>
</c:if>
</div>

<div id="reviewModal" class="modal">
    <c:url value="/book/${book.bookId}/reviews/review" var="postUrl"/>
    <%--@elvariable id="reviewForm" type="reviewForm"--%>
    <form:form
            action="${postUrl}"
            method="post"
            modelAttribute="reviewForm"
    >
        <div class="row">
            <div class="col s12">
                <h5 class="publish-title"><spring:message code="review.title"/></h5>
            </div>
        </div>

        <input type="hidden" id="rating" name="rating" value="rating">
        <div class="input-field center-align">
            <div class="star-rating">
                <i class="material-icons small star" >star_border</i>
                <i class="material-icons small star" >star_border</i>
                <i class="material-icons small star" >star_border</i>
                <i class="material-icons small star" >star_border</i>
                <i class="material-icons small star" >star_border</i>
            </div>
        </div>
        <form:errors path="rating"/>
        <div class="input-field">
            <form:label path="review"/>
            <form:textarea path="review" maxlength="500"/>
        </div>
        <form:errors path="review"/>

        <div class="row">
            <div class="col s12">
                <div class="btn-centerer">
                    <button class="btn modal-close close-btn" ><strong><spring:message code="cancel"/></strong></button>
                    <button class="waves-light btn" type="submit"><strong><spring:message code="review.submit"/></strong></button>
                </div>
            </div>
        </div>

    </form:form>
</div>
</body>
</html>
