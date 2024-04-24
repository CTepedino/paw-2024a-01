<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title>Search</title>
    <link href="${pageContext.request.contextPath}/css/home.css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/searchOptions.css" rel="stylesheet"/>
</head>
<body>
<%@ include file="components/topBar.jsp" %>

<c:url value="/search" var="searchUrl"/>

<form:form
    modelAttribute="bookSearchForm"
    action="${searchUrl}"
    method="get"
>

<div class="row">
    <div class="books col s9">
        <div class="row">
                <c:forEach var="book" items="${books}">
                    <c:set var="book" value="${book}" scope="request"/>
                    <%@include file="components/bookInfoCard.jsp"%>
                </c:forEach>
                <c:if test="${books.size()==0}">
                    <h5><spring:message code="book.search.noBooks"/></h5>
                </c:if>
        </div>
    </div>
    <div class="col s3">
        <div class="row">
            <div class="col s12">

        <form:label path="title"><spring:message code="book.search.title"/></form:label>
        <form:input path="title"/>
            </div>
        </div>

        <div class="row">
            <div class="col s12">
                <div class="input-field">
                    <form:label path="orderBy" cssClass="active"><spring:message code="book.search.orderBy"/></form:label><br>
                    <form:select path="orderBy">
                        <form:option value="" disabled="true"><spring:message code="book.search.selectOrder"/></form:option>
                        <form:option value="PRICE"><spring:message code="book.bookSearchOrderBy.price"/></form:option>
                        <form:option value="PAGE_COUNT"><spring:message code="book.bookSearchOrderBy.pageCount"/></form:option>
                        <form:option value="PUBLICATION_DATE"><spring:message code="book.bookSearchOrderBy.publicationDate"/></form:option>
                    </form:select>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col s6">
                <form:label path="minPrice"><spring:message code="book.search.minPrice"/></form:label>
                <form:input type="number" path="minPrice"/>
            </div>
            <div class="col s6">
                <form:label path="maxPrice"><spring:message code="book.search.maxPrice"/></form:label>
                <form:input type="number" path="maxPrice"/>
            </div>
        </div>

        <div class="row">
            <div class="col s6">
                <form:label path="minPageCount"><spring:message code="book.search.minPages"/></form:label>
                <form:input type="number" path="minPageCount"/>
            </div>
            <div class="col s6">
                <form:label path="maxPageCount"><spring:message code="book.search.maxPages"/></form:label>
                <form:input type="number" path="maxPageCount"/>
            </div>
        </div>

        <div class="row">
            <div class="col s6">
                <form:label path="minSuggestedAge"><spring:message code="book.search.minAge"/></form:label>
                <form:input type="number" path="minSuggestedAge"/>
            </div>
            <div class="col s6">
                <form:label path="maxSuggestedAge"><spring:message code="book.search.maxAge"/></form:label>
                <form:input type="number" path="maxSuggestedAge"/>
            </div>
        </div>

        <div class="row">
            <div class="col s12">
            <div class="input-field">
                <form:label path="genre" cssClass="active"><spring:message code="book.search.genre"/></form:label><br>
                <form:select path="genre">
                    <form:option value="" disabled="true"><spring:message code="book.search.selectGenre"/></form:option>
                    <form:option value="FANTASY"> <spring:message code="book.genre.FANTASY"/> </form:option>
                    <form:option value="NON_FICTION"> <spring:message code="book.genre.NON_FICTION"/> </form:option>
                    <form:option value="MYSTERY"> <spring:message code="book.genre.MYSTERY"/> </form:option>
                    <form:option value="THRILLER"> <spring:message code="book.genre.THRILLER"/> </form:option>
                    <form:option value="ROMANCE"> <spring:message code="book.genre.ROMANCE"/> </form:option>
                    <form:option value="SCIENCE_FICTION"> <spring:message code="book.genre.SCIENCE_FICTION"/> </form:option>
                    <form:option value="FICTION"> <spring:message code="book.genre.FICTION"/> </form:option>
                    <form:option value="HORROR"> <spring:message code="book.genre.HORROR"/> </form:option>
                    <form:option value="HISTORICAL_FICTION"> <spring:message code="book.genre.HISTORICAL_FICTION"/> </form:option>
                    <form:option value="BIOGRAPHY"> <spring:message code="book.genre.BIOGRAPHY"/> </form:option>
                    <form:option value="SELF_HELP"> <spring:message code="book.genre.SELF_HELP"/> </form:option>
                    <form:option value="YOUNG_ADULT"> <spring:message code="book.genre.YOUNG_ADULT"/> </form:option>
                </form:select>
                </div>
            </div>
        </div>
        <div class="input-field">
            <button class="btn waves-effect waves-light" type="submit" name="action">
                <spring:message code="book.search.apply"/>
            </button>
        </div>
    </div>
</div>
</form:form>
<script type="module">
    // Initialize Materialize components
    document.addEventListener('DOMContentLoaded', function() {
        var elems = document.querySelectorAll('.sidenav');
        var instances = M.Sidenav.init(elems);
    });
</script>
</body>
</html>
