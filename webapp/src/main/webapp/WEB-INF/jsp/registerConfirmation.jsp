<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
  <title><spring:message code="session.emailSentTitle"/></title>
  <link href="${pageContext.request.contextPath}/css/userForm.css" rel="stylesheet"/>
</head>

<c:set var="hideSearchBar" value="${true}" scope="request"/>
<jsp:include page="components/topBar.jsp"/>

<body>

<div class="small-container">
  <div class="form">
    <h5 class="center-align">
      <spring:message code="session.emailSent"/>
    </h5>

    <p>
      <spring:message code="session.emailSentInstructions"/>
    </p>

  </div>
</div>

</body>
</html>
