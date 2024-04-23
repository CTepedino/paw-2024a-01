<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cybrary</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/orderSummary.css">
</head>
<%@ include file="components/topBar.jsp" %>
<body>
<div class="orderCards">
    <img class="hero-image" src="${pageContext.request.contextPath}/images/order_summary.svg">
    <div class="container">
        <h2 class="title">Your information has been sent!</h2>
        <p class="order-description">Await for further instructions in your inbox. </p>
        <button class="proceed-button"><a class="a-button" href="${pageContext.request.contextPath}/">View my orders</a>
        </button>
        <button class="cancel-button"><a class="cancel-button" href="${pageContext.request.contextPath}/">Return home</a>
            </button>
    </div>
</div>
</body>

</html>
