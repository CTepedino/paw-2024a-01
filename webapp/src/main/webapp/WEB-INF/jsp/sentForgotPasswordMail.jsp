<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
  <title><spring:message code="session.resetPassword"/></title>
  <link href="<c:url value="/css/userForm.css"/>" rel="stylesheet"/>

  <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/images/cybrary.png"/>"/>
</head>

<c:set value="${true}" scope="request" var="hideSearchBar"/>
<c:set value="${true}" var="hideRightBar" scope="request"/>
<%@include file="components/topBar.jsp" %>

<body>
<div class="small-container">
  <div class="form">
    <div class="formnt z-depth-2">
      <h5 class="center-align"><spring:message code="session.resetCodeSent"/></h5>

      <p class="center-align"><spring:message code="session.resetCodeSentInstructions"/></p>

      <br/>
      <br/>

      <div class="input-field center-align submit-btn">
        <form method="post" action="<c:url value="/resendResetCode/${id}"/>">
          <button type="submit" class="btn waves-effect waves-light white-text">
            <strong><spring:message code="session.resendResetCode"/></strong>
          </button>
        </form>
      </div>
    </div>
  </div>
</div>
</body>
</html>
