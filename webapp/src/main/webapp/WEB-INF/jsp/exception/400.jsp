<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
    <title><spring:message code="error.400.title"/></title>
    <link rel="stylesheet" href="<c:url value="/css/userForm.css"/>">
</head>


<c:set value="${true}" var="hideSearchBar" scope="request"/>
<%@include file="../components/topBar.jsp" %>

<body>
<div class="small-container">
    <div class="form">
        <div class="formnt z-depth-2">
            <h5 class="center-align"><spring:message code="error.400.title"/></h5>

            <p class="center-align"><spring:message code="error.400.description"/></p>

            <br/>
            <div class="input-field center-align submit-btn">
                <a class="btn waves-effect waves-light white-text" href="<c:url value="/"/>">
                    <spring:message code="error.callToAction"/>
                </a>
            </div>
        </div>
    </div>
</div>
</body>
</html>
