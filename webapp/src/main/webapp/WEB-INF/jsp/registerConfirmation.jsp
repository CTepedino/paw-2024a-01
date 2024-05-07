<%--<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>--%>
<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>--%>

<%--<html>--%>
<%--<head>--%>
<%--  <title>Confirmation</title>--%>
<%--  <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/images/cybrary.png" />--%>
<%--  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">--%>
<%--  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/emailConfirmation.css">--%>
<%--</head>--%>

<%--<body>--%>
<%--<div class="container">--%>
<%--  <div class="card-panel teal lighten-2 white-text">--%>
<%--    <h4>Your account has been registered correctly!</h4>--%>
<%--    <p>Please Sign In to have access to your new account</p>--%>
<%--  </div>--%>
<%--  <a class="waves-effect waves-light btn" href="${pageContext.request.contextPath}/login">Sign In</a>--%>
<%--</div>--%>


<%--</body>--%>
<%--</html>--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Confirmation</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/orderSummary.css">
</head>
<%--<%@ include file="components/topBar.jsp" %>--%>
<body>
<div class="orderCards">
  <img class="hero-image" src="${pageContext.request.contextPath}/images/registerConfirmation.svg">
  <div class="container">
    <h2 class="title">Your account has been registered correctly!</h2>
    <p class="order-description">Please Sign In to have access to your new account </p>
    <button class="proceed-button"><a class="a-button" href="${pageContext.request.contextPath}/login">Sign In</a>
<%--    </button>--%>
<%--    <button class="cancel-button"><a class="cancel-button" href="${pageContext.request.contextPath}/">Return back</a>--%>
<%--    </button>--%>
  </div>
</div>
</body>

</html>
