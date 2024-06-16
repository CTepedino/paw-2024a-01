<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<spring:eval expression="@environment.getProperty('base.url')" var="baseUrl"/>
<html>
<head>
    <title><spring:message code="profile.analytics"/></title>
    <script src="https://kit.fontawesome.com/0f001c5d7a.js" crossorigin="anonymous"></script>
    <link href="<c:url value="/css/dashboard.css"/>" rel="stylesheet"/>

    <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/images/cybrary.png"/>"/>
</head>
<%@include file="components/topBar.jsp" %>
<body>
    <div class="container dashboard">
        <h2 class="page-title"><spring:message code="profile.analytics"/></h2>
        <div class="row">
            <div class="col s4 m5">
                <div class="card-panel">
                    <span class="white-text">
                    </span>
                </div>
            </div>
            <div class="col s4 m5">
                <div class="card-panel">
                    <span class="white-text">
                    </span>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
