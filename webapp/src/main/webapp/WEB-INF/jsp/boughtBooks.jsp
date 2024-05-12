<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<spring:eval expression="@environment.getProperty('base.url')" var="baseUrl"/>
<html>
<head>
    <title><spring:message code="publication.myBooks.title"/></title>
    <link href="${pageContext.request.contextPath}/css/myBooks.css" rel="stylesheet"/>
    <link href="<c:url value="/css/paginationControls.css"/>" rel="stylesheet"/>
</head>


<%@include file="profile.jsp" %>

<body>
<c:url value="/profile/${userId}/readBooks" var="readBooksUrl"/>
<div class="container myBooks">
    <form:form modelAttribute="myBooksSearchForm"
               action="${readBooksUrl}"
               method="get"
               id="books"
    >
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
                const paginationButtons = new PaginationButtons(${books.pageCount}, Math.min(10, ${books.pageCount}), ${books.pageNumber}, true);
                paginationButtons.render();
                paginationButtons.onChange(e => {
                    document.getElementById('page').value = e.target.value;
                    document.getElementById("books").submit();
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
