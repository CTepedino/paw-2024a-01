<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
    <spring:message var="pageTitle" code="book.search.pageTitle" arguments="${bookSearchForm.title}"/>
    <title><c:out value="${pageTitle}"/></title>
    <link href="${pageContext.request.contextPath}/css/home.css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/searchOptions.css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/paginationControls.css" rel="stylesheet"/>
</head>
<jsp:include page="components/topBar.jsp">
    <jsp:param name="hasWriterRole" value="${hasWriterRole}" />
    <jsp:param name="hideSearchBar" value="true"/>
</jsp:include>
<body>

<c:url value="/search" var="searchUrl"/>

<form:form
    modelAttribute="bookSearchForm"
    action="${searchUrl}"
    method="get"
    id="search"
>

<div class="row">
    <div class="books col s9">
        <c:if test="${books.page.size()==0}">
            <div class="no-books">
                <h5 class="center center-align">
                    <spring:message code="book.search.noBooks"/>
                </h5>
            </div>

        </c:if>
        <c:if test="${books.page.size()>0}">
            <div class="row">
                    <c:forEach var="book" items="${books.page}">
                        <c:set var="book" value="${book}" scope="request"/>
                        <%@include file="components/bookInfoCard.jsp"%>
                    </c:forEach>
            </div>
            <div class="row">
                <c:if test="${books.pageCount > 1}">
                    <input type="number" id="page" name="page" value="${books.pageNumber}" style="display: none">
                    <script src="<c:url value="/js/paginationControls.js"/>"></script>
                    <script>
                        const paginationButtons = new PaginationButtons(${books.pageCount}, Math.min(10, ${books.pageCount}), ${books.pageNumber}, true);
                        paginationButtons.render();
                        paginationButtons.onChange(e => {
                            document.getElementById('page').value = e.target.value;
                            document.getElementById("search").submit();
                        })
                    </script>
                </c:if>
            </div>
        </c:if>
    </div>
    <div class="col s3">
        <div class="row">
            <div class="col s12">

        <form:label path="title">
            <spring:message code="book.search.title"/>
        </form:label>
        <form:input path="title" required="true"/>
            </div>
        </div>

        <div class="row">
            <div class="col s12">
                <div class="input-field">
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
        </div>



        <div class="row">
            <div class="col s6">
                <form:label path="minPrice">
                    <spring:message code="book.search.minPrice"/>
                </form:label>
                <form:input type="number" path="minPrice" step=".01" min="0"/>
            </div>
            <div class="col s6">
                <form:label path="maxPrice">
                    <spring:message code="book.search.maxPrice"/>
                </form:label>
                <form:input type="number" path="maxPrice" step=".01" min="0"/>
            </div>
        </div>

        <div class="row">
            <form:errors path="minPrice" cssClass="red-text" element="p"/>
            <form:errors path="maxPrice" cssClass="red-text" element="p"/>
        </div>


        <div class="row">
            <div class="col s6">
                <form:label path="minPageCount">
                    <spring:message code="book.search.minPages"/>
                </form:label>
                <form:input type="number" path="minPageCount" min="0"/>
            </div>
            <div class="col s6">
                <form:label path="maxPageCount">
                    <spring:message code="book.search.maxPages"/>
                </form:label>
                <form:input type="number" path="maxPageCount" min="0"/>
            </div>
        </div>

        <div class="row">
            <form:errors path="minPageCount" cssClass="red-text" element="p"/>
            <form:errors path="maxPageCount" cssClass="red-text" element="p"/>
        </div>

        <div class="row">
            <div class="col s6">
                <form:label path="minSuggestedAge">
                    <spring:message code="book.search.minAge"/>
                </form:label>
                <form:input type="number" path="minSuggestedAge" min="0"/>
            </div>
            <div class="col s6">
                <form:label path="maxSuggestedAge">
                    <spring:message code="book.search.maxAge"/>
                </form:label>
                <form:input type="number" path="maxSuggestedAge" min="0"/>
            </div>
        </div>

        <div class="row">
            <form:errors path="minSuggestedAge" cssClass="red-text" element="p"/>
            <form:errors path="maxSuggestedAge" cssClass="red-text" element="p"/>
        </div>

        <div class="row">
            <div class="col s12">
            <div class="input-field">
                <form:label path="genre" cssClass="active">
                    <spring:message code="book.search.genre"/>
                </form:label><br>
                <form:select path="genre" onchange="this.form.submit()">
                    <form:option value=""><spring:message code="book.search.selectGenre"/></form:option>
                    <c:forEach items="${genres}" var="genre">
                        <form:option value="${genre}"><spring:message code="book.genre.${genre}"/></form:option>
                    </c:forEach>
                </form:select>
                </div>
            </div>
        </div>
        <div class="input-field center">
            <button class="btn waves-effect waves-light white-text" type="submit" name="action">
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

    document.addEventListener('DOMContentLoaded', function () {
        var elems = document.querySelectorAll('select');
        var instances = M.FormSelect.init(elems);
    });
</script>
</body>
</html>
