
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
    <title>Cybrary</title>
    <link href="<c:url value="/css/home.css"/>" rel="stylesheet"/>
    <link href="<c:url value="/css/paginationControls.css"/>" rel="stylesheet"/>
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/images/cybrary.png"/>"/>

    <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/images/cybrary.png"/>"/>
</head>

<body>

<%@include file="components/topBar.jsp" %>

<div class="explore_back">
    <div class="container hide-on-small-only">
            <div class="row">
                <div class="col s4">
                    <div class="card-panel center">
                        <img class="home-image" src="<c:url value="/images/searchBook.svg"/>">
                        <div class="home-card-content">
                            <h6 class="home-steps"><spring:message code="book.home.findBook"/></h6>
                        </div>
                    </div>
                </div>
                <div class="col s4">
                    <div class="card-panel center">
                        <img class="home-image" src="<c:url value="/images/contactWriter.svg"/>">
                        <div class="home-card-content">
                            <h6 class="home-steps"><spring:message code="book.home.contactWriter"/></h6>
                        </div>
                    </div>
                </div>
                <div class="col s4">
                    <div class="card-panel center">
                        <img class="home-image" src="<c:url value="/images/contact.svg"/>">
                        <div class="home-card-content">
                            <h6 class="home-steps"><spring:message code="book.home.getCopy"/></h6>
                        </div>
                    </div>
                </div>
            </div>
</div>

    <c:url var="searchUrl" value="/search?"/>
    <div class="row hide-on-med-and-down">
        <c:forEach items="${popularGenres}" var="genre">
            <div class="col s2">
                <a class="genre-btn waves-effect waves-light btn" href="${searchUrl}genre=${genre}&page=1"><i class="material-icons left">${genre.iconName}</i><spring:message code="book.genre.${genre}"/></a>
            </div>
        </c:forEach>
    </div>

    <div class="row">
        <c:forEach var="book" items="${books.page}">
            <c:set var="book" value="${book}" scope="request"/>
            <%@include file="components/bookInfoCard.jsp"%>
        </c:forEach>
    </div>

    <c:if test="${books.pageCount > 1}">
        <script src="<c:url value="/js/paginationControls.js"/>"></script>
        <script>
            const paginationButtons = new PaginationButtons(${books.pageCount}, Math.min(10, ${books.pageCount}), ${books.pageNumber}, false);
            paginationButtons.render();
            paginationButtons.onChange(e => {
                window.location.href = "<c:url value="?page="/>" + e.target.value;
            })
        </script>
    </c:if>
</body>
</html>
