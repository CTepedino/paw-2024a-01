<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
    <title><spring:message code="session.validationTitle"/></title>
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
            <h5 class="center-align"><spring:message code="session.validationSuccess"/></h5>

            <p class="center-align"><spring:message code="session.validationSuccessInstructions"/></p>

            <br/>
            <br/>

            <div class="input-field center-align submit-btn">
                <a class="btn waves-effect waves-light white-text" href="<c:url value="/"/>">
                    <spring:message code="session.toHome"/>
                </a>
            </div>
        </div>
    </div>
</div>
</body>
</html>
