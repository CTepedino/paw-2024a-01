<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="buy.emailConfirmation.title"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/orderSummary.css">
</head>

<!--TODO: mejorar instrucciones -->
<body>
<div class="orderCards">
    <img
            class="hero-image"
            src="${pageContext.request.contextPath}/images/order_summary.svg"
            alt="<spring:message code="buy.emailConfirmation.imageAlt"/>"
    />
    <div class="container">
        <h3 class="title">
            <spring:message code="buy.emailConfirmation.sentInfo"/>
        </h3>
        <p class="order-description">
            <spring:message code="buy.emailConfirmation.instructions" arguments="${order.book.price},${order.writer.cbu}"/>
        </p>
        <button class="cancel-button">
            <a class="cancel-button" href="${pageContext.request.contextPath}/">
                <spring:message code="buy.emailConfirmation.sendReceipt"/>
            </a>
        </button>
        <div class="divider"></div>
        <p><spring:message code="buy.emailConfirmation.sendLater"/></p>
        <button class="proceed-button">
            <a class="a-button" href="${pageContext.request.contextPath}/purchases">
                <spring:message code="buy.emailConfirmation.viewMyOrders"/>
            </a>
        </button>
    </div>
</div>
</body>

</html>
