<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
  <title><spring:message code="session.emailSentTitle"/></title>
  <link href="<c:url value="/css/userForm.css"/>" rel="stylesheet"/>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title><spring:message code="session.emailSent"/></title>
  <link rel="stylesheet" href="<c:url value="/css/orderSummary.css"/>">

  <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/images/cybrary.png"/>"/>
</head>

<c:set value="${true}" scope="request" var="hideSearchBar"/>
<%@include file="components/topBar.jsp" %>

<body>
<div class="small-container">
  <div class="form">
    <div class="formnt z-depth-2">
      <h5 class="center-align">
        <spring:message code="session.emailSent"/>
      </h5>
      <br/>
      <br/>
      <p class="center-align">
        <spring:message code="session.emailSentInstructions"/>
      </p>
    </div>
  </div>
</div>
</body>
</html>
