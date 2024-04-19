<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>Search</title>
    <link href="${pageContext.request.contextPath}/css/home.css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/searchOptions.css" rel="stylesheet"/>
</head>
<body>
<%@ include file="components/topBar.jsp" %>
<div class="books">
    <div class="container">
        <c:forEach var="book" items="${books}">
            <c:set var="book" value="${book}" scope="request"/>
            <%@include file="components/bookInfoCard.jsp"%>
        </c:forEach>
        <c:if test="${books.size()==0}">
            <h1>NO BOOKS :(</h1>
        </c:if>
    </div>
</div>
</body>
</html>
