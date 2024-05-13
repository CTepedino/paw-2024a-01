<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>
<html>
<head>
    <title><c:out value="${book.title}"/></title>
    <link href="<c:url value="/css/bookInfo.css"/>" rel="stylesheet"/>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons"/>
    <link rel="stylesheet" href="<c:url value="/css/reviews.css"/>"/>

    <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/images/cybrary.png"/>"/>
</head>

<body>
<script src="<c:url value="/js/starRating.js"/>"></script>
<div class="card row">
        <div class="col s4 l2 reviewer-info">
            <a class="reviewer-info-stacked" href="<c:url value="/profile/${review.reviewer.userId}"/>">
                <div class="image-centerer">
                    <img
                            src="/profilePicture/${review.reviewer.userId}"
                            class="review-pfp "
                            alt="<spring:message code="user.profile.edit.pfp"/>"
                    >
                </div>
                <p class="text-center">${review.reviewer.firstName} ${review.reviewer.lastName}</p>
            </a>
        </div>
        <div class="col s8 l10 review-box">
            <div class="row">
                <div class="stars col s6">
                    <script>
                        new FixedStarRating(${review.rating})
                    </script>
                </div>
                <div class="col s6 date-holder">
                    <p class="review-date">${review.getFormattedDate(pageContext.request.locale)}</p>
                </div>
            </div>
            <p>${review.review}</p>
        </div>
</div>
</body>
</html>
