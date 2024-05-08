<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
  <title><spring:message code="session.emailSentTitle"/></title>
  <link href="${pageContext.request.contextPath}/css/userForm.css" rel="stylesheet"/>
</head>

<jsp:include page="components/topBar.jsp">
  <jsp:param name="hasWriterRole" value="${isWriter}" />
  <jsp:param name="hideSearchBar" value="${true}"/>
</jsp:include>
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
