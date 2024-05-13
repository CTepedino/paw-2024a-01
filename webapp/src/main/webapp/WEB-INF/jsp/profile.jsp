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
<c:url value="/profile/${user.userId}/${tab}" var="readBooksUrl"/>
<div class="container myBooks">
    <form:form
            modelAttribute="profileBookSearchForm"
            action="${readBooksUrl}"
            method="get"
            id="bookSearch"
    >
        <input type="submit" hidden/>
        <div class="row">
            <div class="input-field col s6">
                <form:label path="title" cssClass="active">
                    <spring:message code="book.search.title"/>
                </form:label><br>
                <form:input path="title"/>
            </div>
            <div class="input-field col s6">
                <form:label path="orderBy" cssClass="active">
                    <spring:message code="book.search.orderBy"/>
                </form:label><br>
                <form:select path="orderBy" onchange="this.form.submit()">
                    <c:forEach items="${orders}" var="order">
                        <form:option value="${order}"><spring:message code="book.bookSearchOrderBy.${order.messageCode}"/></form:option>
                    </c:forEach>
                </form:select>
            </div>
        </div>

        <div class="row">
            <c:if test="${empty books.page and books.pageNumber eq 1}">
                <h5 class="empty-books"><spring:message code="profile.noBooks"/></h5>
            </c:if>
            <c:forEach var="book" items="${books.page}">
                <c:set var="cardBook" value="${book}" scope="request"/>
                <c:set var="ownsProfile" value="${ownsProfile}" scope="request"/>
                <c:set var="publications" value="${false}" scope="request"/>
                <%@include file="components/smallBookCard.jsp"%>
            </c:forEach>
        </div>

        <c:if test="${books.pageCount > 1}">
            <input type="number" id="page" name="page" value="${books.pageNumber}" style="display: none"/>
            <script src="<c:url value="/js/paginationControls.js"/>"></script>
            <script>
                const paginationButtons = new PaginationButtons(${books.pageCount}, Math.min(10, ${books.pageCount}), ${books.pageNumber}, false);
                paginationButtons.render();
                paginationButtons.onChange(e => {
                    document.getElementById('page').value = e.target.value;
                    document.getElementById("bookSearch").submit();
                })
            </script>
        </c:if>
    </form:form>
</div>
<script type="module">
    document.addEventListener('DOMContentLoaded', function() {
        var elems = document.querySelectorAll('.sidenav');
        var instances = M.Sidenav.init(elems);
    });
    document.addEventListener('DOMContentLoaded', function () {
        var elems = document.querySelectorAll('select');
        var instances = M.FormSelect.init(elems);
    });
</script>
</body>
</html>
