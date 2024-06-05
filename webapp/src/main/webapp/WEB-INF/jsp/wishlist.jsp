<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <link href="<c:url value="/css/myBooks.css"/>" rel="stylesheet"/>
    <link href="<c:url value="/css/paginationControls.css"/>" rel="stylesheet"/>

    <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/images/cybrary.png"/>"/>
</head>


<%@include file="profileTab.jsp" %>

<body>
    <div class="row">
        <c:if test="${empty wishlist.page and wishlist.pageNumber eq 1}">
            <h5 class="empty-books"><spring:message code="profile.noBooks"/></h5>
        </c:if>
        <c:forEach var="book" items="${wishlist.page}">
            <c:set var="cardBook" value="${book}" scope="request"/>
            <c:set var="ownsProfile" value="${true}" scope="request"/>
            <%@include file="components/smallBookCard.jsp"%>
        </c:forEach>
    </div>

    <c:if test="${wishlist.pageCount > 1}">
        <input type="submit" hidden />
        <input name="page" id="page" style="display: none"/>
        <script src="<c:url value="/js/paginationControls.js"/>"></script>
        <script>
            const paginationButtons = new PaginationButtons(${wishlist.pageCount}, Math.min(10, ${wishlist.pageCount}), ${wishlist.pageNumber}, false);
            paginationButtons.render();
            paginationButtons.onChange(e => {
                document.getElementById('page').value = e.target.value;
                document.getElementById("bookSearch").submit();
            })
        </script>
    </c:if>
</body>