<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
    <meta charset="UTF-8">
    <title><spring:message code="book.bookInfo.createDeal"/></title>
    <link href="<c:url value="/css/addBook.css"/>" rel="stylesheet"/>

    <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/images/cybrary.png"/>"/>
</head>

<c:set value="${true}" scope="request" var="hideSearchBar"/>
<%@include file="components/topBar.jsp" %>

<body>
<div class="container publish-container">
    <div class="form">
        <c:url value="/book/${bookId}/deal" var="postUrl"/>
        <form:form
                modelAttribute="dealForm"
                action="${postUrl}"
                method="post"
                enctype="multipart/form-data"
                cssClass="z-depth-2"
        >
            <h5 class="publish-title"><spring:message code="book.addDeal.title"/></h5>

            <h6><c:out value="${book.title}"/></h6>

            <p><spring:message code="book.addDeal.previousPrice"/> <c:out value="${book.formattedPrice}"/></p>

            <div class="input-field">
                <form:label path="price"><spring:message code="book.addDeal.newPrice"/><span class="red-text">*</span></form:label>
                <form:input type="number" path="price" min="0" max="100000000"/>
            </div>
            <form:errors path="price" cssClass="red-text"  element="p"/>
            <br>

            <div class="input-field">
                <form:label path="duration"><spring:message code="book.addDeal.dealDuration"/><span class="red-text">*</span></form:label>
                <form:input type="number" path="duration" min="0" max="100"/>
            </div>
            <form:errors path="duration" cssClass="red-text"  element="p"/>
            <br>


            <div class="input-field center">
                <button class="btn waves-effect waves-light" type="submit" name="action">
                    <strong><spring:message code="book.addDeal.createDeal"/></strong>
                </button>
            </div>
        </form:form>
    </div>
</div>
</body>
</html>
