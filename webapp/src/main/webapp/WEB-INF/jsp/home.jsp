
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:eval expression="@environment.getProperty('base.url')" var="baseUrl"/>

<!DOCTYPE html>
<html>
<head>
    <title>Cybrary</title>
    <link href="${pageContext.request.contextPath}/css/home.css" rel="stylesheet"/>
    <link href="<c:url value="/css/paginationControls.css"/>" rel="stylesheet"/>
</head>

<jsp:include page="components/topBar.jsp">
    <jsp:param name="hasWriterRole" value="${isWriter}" />
</jsp:include>

<body>
    <div class="explore_back">
    <div class="container">
            <div class="row">
                <div class="col s4">
                    <div class="card-panel center">
                        <img class="home-image" src="${pageContext.request.contextPath}/images/transparent-background-book-24.png">
                        <h6 class="white-text"><spring:message code="book.home.findBook"/></h6>
                    </div>
                </div>
                <div class="col s4">
                    <div class="card-panel center">
                        <img class="home-image" src="${pageContext.request.contextPath}/images/handshake.png">
                        <h6 class="white-text"><spring:message code="book.home.contactWriter"/></h6>
                    </div>
                </div>
                <div class="col s4">
                    <div class="card-panel center">
                        <img class="home-image" src="${pageContext.request.contextPath}/images/book.png">
                        <h6 class="white-text"><spring:message code="book.home.getCopy"/></h6>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!--TODO: reemplazar por forEach-->
    <c:url var="searchUrl" value="/search?"/>
        <div class="row">
            <div class="col s2">
                <a class="genre-btn waves-effect waves-light btn" href="${searchUrl}genre=FANTASY&page=1"><i class="material-icons left">auto_fix_high</i><spring:message code="book.genre.FANTASY"/></a>
            </div>
            <div class="col s2">
                <a class="genre-btn waves-effect waves-light btn" href="${searchUrl}genre=NON_FICTION&page=1"><i class="material-icons left">newspaper</i><spring:message code="book.genre.NON_FICTION"/></a>
            </div>
            <div class="col s2">
                <a class="genre-btn waves-effect waves-light btn" href="${searchUrl}genre=MYSTERY&page=1"><i class="material-icons left">search</i><spring:message code="book.genre.MYSTERY"/></a>
            </div>
            <div class="col s2">
                <a class="genre-btn waves-effect waves-light btn" href="${searchUrl}genre=THRILLER&page=1"><i class="material-icons left">directions_run</i><spring:message code="book.genre.THRILLER"/></a>
            </div>
            <div class="col s2">
                <a class="genre-btn waves-effect waves-light btn" href="${searchUrl}genre=ROMANCE&page=1"><i class="material-icons left">favorite</i><spring:message code="book.genre.ROMANCE"/></a>
            </div>
            <div class="col s2">
                <a class="genre-btn waves-effect waves-light btn" href="${searchUrl}genre=SCIENCE_FICTION&page=1"><i class="material-icons left">psychology_alt</i><spring:message code="book.genre.SCIENCE_FICTION"/></a>
            </div>
        </div>
    <div class="row">
        <div class="col s2">
            <a class="genre-btn waves-effect waves-light btn" href="${searchUrl}genre=FICTION&page=1"><i class="material-icons left">menu_book</i><spring:message code="book.genre.FICTION"/></a>
        </div>
        <div class="col s2">
            <a class="genre-btn waves-effect waves-light btn" href="${searchUrl}genre=HORROR&page=1"><i class="material-icons left">mood_bad</i><spring:message code="book.genre.HORROR"/></a>
        </div>
        <div class="col s2">
            <a class="genre-btn waves-effect waves-light btn" href="${searchUrl}genre=HISTORICAL_FICTION&page=1"><i class="material-icons left">history_edu</i><spring:message code="book.genre.HISTORICAL_FICTION"/></a>
        </div>
        <div class="col s2">
            <a class="genre-btn waves-effect waves-light btn" href="${searchUrl}genre=BIOGRAPHY&page=1"><i class="material-icons left">person</i><spring:message code="book.genre.BIOGRAPHY"/></a>
        </div>
        <div class="col s2">
            <a class="genre-btn waves-effect waves-light btn" href="${searchUrl}genre=SELF_HELP&page=1"><i class="material-icons left">healing</i><spring:message code="book.genre.SELF_HELP"/></a>
        </div>
        <div class="col s2">
            <a class="genre-btn waves-effect waves-light btn" href="${searchUrl}genre=YOUNG_ADULT&page=1"><i class="material-icons left">local_play</i><spring:message code="book.genre.YOUNG_ADULT"/></a>
        </div>
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
