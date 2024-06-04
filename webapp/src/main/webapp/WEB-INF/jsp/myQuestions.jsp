<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
    <title><spring:message code="topBar.questions"/></title>
    <script src="https://kit.fontawesome.com/0f001c5d7a.js" crossorigin="anonymous"></script>
    <link href="<c:url value="/css/questions.css"/>" rel="stylesheet"/>
    <link href="<c:url value="/css/paginationControls.css"/>" rel="stylesheet"/>

    <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/images/cybrary.png"/>"/>
</head>
<body>
    <h2 class="page-title"><spring:message code="orders.purchases.header"/></h2>
</body>
</html>
