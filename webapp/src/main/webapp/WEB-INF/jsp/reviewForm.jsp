<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <title><spring:message code="review.title"/></title>
    <link rel="stylesheet" href="<c:url value="/css/starRating.css"/>"/>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons"/>
</head>


<%@include file="components/topBar.jsp" %>

<body>

<c:url value="/book/${bookId}/review" var="postUrl"/>
<form:form
    action="${postUrl}"
    method="post"
    modelAttribute="reviewForm"
>
    <input type="hidden" id="rating" name="rating" value="rating">
    <div class="input-field center-align">
        <div class="star-rating">
            <i class="material-icons small star" onclick="setRating()">star_border</i>
            <i class="material-icons small star" onclick="setRating()">star_border</i>
            <i class="material-icons small star" onclick="setRating()">star_border</i>
            <i class="material-icons small star" onclick="setRating()">star_border</i>
            <i class="material-icons small star" onclick="setRating()">star_border</i>
        </div>
    </div>
    <form:errors path="rating"/>
    <div class="input-field">
        <form:label path="review"/>
        <form:textarea path="review"/>
    </div>
    <form:errors path="review"/>

    <button class="waves-light btn" type="submit">Submit Review</button>
</form:form>

<script src="<c:url value="/js/selectableStarRating.js"/>"></script>
<script>
    initializeRating(${reviewForm.rating});
</script>

</body>
</html>
