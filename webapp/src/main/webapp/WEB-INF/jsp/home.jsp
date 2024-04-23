
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:eval expression="@environment.getProperty('base.url')" var="baseUrl"/>

<html>
<head>
    <title>Cybrary</title>
    <link href="${pageContext.request.contextPath}/css/home.css" rel="stylesheet"/>
</head>
<%@ include file="components/topBar.jsp" %>
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
    <c:url var="searchUrl" value="/search?"/>
        <div class="row">
            <div class="col s2">
                <a class="genre-btn waves-effect waves-light btn" href="${searchUrl}genre=FANTASY"><i class="material-icons left">auto_fix_high</i>Fantasy</a>
            </div>
            <div class="col s2">
                <a class="genre-btn waves-effect waves-light btn" href="${searchUrl}genre=NON_FICTION"><i class="material-icons left">newspaper</i>Non Fiction</a>
            </div>
            <div class="col s2">
                <a class="genre-btn waves-effect waves-light btn" href="${searchUrl}genre=MYSTERY"><i class="material-icons left">search</i>Mystery</a>
            </div>
            <div class="col s2">
                <a class="genre-btn waves-effect waves-light btn" href="${searchUrl}genre=THRILLER"><i class="material-icons left">directions_run</i>Thriller</a>
            </div>
            <div class="col s2">
                <a class="genre-btn waves-effect waves-light btn" href="${searchUrl}genre=ROMANCE"><i class="material-icons left">favorite</i>Romance</a>
            </div>
            <div class="col s2">
                <a class="genre-btn waves-effect waves-light btn" href="${searchUrl}genre=SCIENCE_FICTION"><i class="material-icons left">psychology_alt</i>Science Fiction</a>
            </div>
        </div>
    <div class="row">
        <div class="col s2">
            <a class="genre-btn waves-effect waves-light btn" href="${searchUrl}genre=FICTION"><i class="material-icons left">menu_book</i>Fiction</a>
        </div>
        <div class="col s2">
            <a class="genre-btn waves-effect waves-light btn" href="${searchUrl}genre=HORROR"><i class="material-icons left">mood_bad</i>Horror</a>
        </div>
        <div class="col s2">
            <a class="genre-btn waves-effect waves-light btn" href="${searchUrl}genre=HISTORICAL_FICTION"><i class="material-icons left">history_edu</i>Historical Fiction</a>
        </div>
        <div class="col s2">
            <a class="genre-btn waves-effect waves-light btn" href="${searchUrl}genre=BIOGRAPHY"><i class="material-icons left">person</i>Biography</a>
        </div>
        <div class="col s2">
            <a class="genre-btn waves-effect waves-light btn" href="${searchUrl}genre=SELF_HELP"><i class="material-icons left">healing</i>Self Help</a>
        </div>
        <div class="col s2">
            <a class="genre-btn waves-effect waves-light btn" href="${searchUrl}genre=YOUNG_ADULT"><i class="material-icons left">local_play</i>Young Adult</a>
        </div>
    </div>

    <div class="books">
        <div class="container">
        <c:forEach var="book" items="${books}">
            <c:set var="book" value="${book}" scope="request"/>
            <%@include file="components/bookInfoCard.jsp"%>
        </c:forEach>
        </div>
    </div>
</body>
</html>
