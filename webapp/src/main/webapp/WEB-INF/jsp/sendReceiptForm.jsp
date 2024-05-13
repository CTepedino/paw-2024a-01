<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><spring:message code="buy.emailConfirmation.sendReceipt"/></title>
    <link href="<c:url value="/css/addBook.css"/>" rel="stylesheet"/>
</head>

<c:set value="${true}" scope="request" var="hideSearchBar"/>
<%@include file="components/topBar.jsp" %>

<body>
<div class="container">
    <div class="form">
        <c:url value="/sendBuyInfo/${book.bookId}" var="postUrl"/>
        <form:form
                modelAttribute="createOrderForm"
                action="${postUrl}"
                method="post"
                enctype="multipart/form-data"
                cssClass="z-depth-2"
        >
            <h5 class="publish-title"><spring:message code="buy.book.title"/></h5>
            <br>
            <h6><spring:message code="buy.book.description"/></h6>

            <h6><spring:message code="buy.book.verification"/></h6>
            <br>
            <h6>
                <spring:message var="price" code="buy.book.transfer" arguments="${book.formattedPrice}"/>
                <c:out value="${price}"/>
            </h6>
            <br>
            <h6>
                <spring:message var="cbu" code="buy.book.cbu" arguments="${book.writer.cbu}"/>
                <c:out value="${cbu}"/>
            </h6>
            <br>
            <div class="file-field input-field">
                <div class="btn">
                    <span><spring:message code="orders.sales.status.seeReceipt"/></span>
                    <input type="file" accept="application/pdf" name="receipt" id="receipt">
                </div>
                <div class="file-path-wrapper">
                    <input class="file-path validate" type="text" \>
                </div>
            </div>
            <form:errors path="receipt" cssClass="red-text"  element="p"/>


            <div class="input-field center">
                <button class="btn waves-effect waves-light" type="submit" name="action">
                    <strong><spring:message code="orders.purchases.action.WAITING_PAYMENT"/></strong>
                </button>
            </div>
        </form:form>
    </div>
</div>
<script>

    document.addEventListener('DOMContentLoaded', function () {
        var elems = document.querySelectorAll('select');
        var instances = M.FormSelect.init(elems);
    });
</script>
</body>
</html>
