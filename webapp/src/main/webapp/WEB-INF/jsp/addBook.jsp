<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><spring:message code="book.addBook.publish"/></title>
    <link href="${pageContext.request.contextPath}/css/addBook.css" rel="stylesheet"/>
</head>

<jsp:include page="components/topBar.jsp">
    <jsp:param name="hasWriterRole" value="${isWriter}" />
    <jsp:param name="hideSearchBar" value="${true}"/>
</jsp:include>

<body>
<div class="container">
    <div class="form">
        <h5><spring:message code="book.addBook.pageTitle"/></h5>
        <c:url value="/addBook" var="postUrl"/>
        <form:form
                modelAttribute="newBookForm"
                action="${postUrl}"
                method="post"
                enctype="multipart/form-data"
                cssClass="z-depth-2"
        >
            <c:if test="${!hasWriterRole}">
                <h6><spring:message code="book.addBook.cbuTitle"/></h6>
                <div class="input-field">
                    <form:label path="cbu"><spring:message code="book.addBook.cbu"/><span class="red-text">*</span></form:label><br>
                    <form:input type="text" path="cbu" inputmode="numeric"/>
                </div>
                <form:errors path="cbu" cssClass="red-text" element="p"/>
            </c:if>

            <h6><spring:message code="book.addBook.bookTitle"/></h6>

            <div class="input-field">
                <form:label path="title"><spring:message code="book.addBook.title"/><span class="red-text">*</span></form:label>
                <form:input type="text" path="title"/>
            </div>
            <form:errors path="title" cssClass="red-text" element="p"/>
            <br>

            <div class="input-field">
                <form:label path="description"><spring:message code="book.addBook.description"/><span class="red-text">*</span></form:label>
                <form:input type="text" path="description"/>
            </div>
            <form:errors path="description" cssClass="red-text" element="p"/>
            <br>
            <div class="input-field">
                <form:label path="genre" cssClass="active"><spring:message code="book.addBook.genre"/><span class="red-text">*</span></form:label>
                <form:select path="genre">
                    <form:option value="" disabled="true"> <spring:message code="book.addBook.genreTitle"/> </form:option>
                    <c:forEach items="${genres}" var="genre">
                        <form:option value="${genre}"><spring:message code="book.genre.${genre}"/></form:option>
                    </c:forEach>
                </form:select>
            </div>
            <form:errors cssClass="red-text"  path="genre" element="p"/>
            <br>
            <div class="input-field">
                <form:label path="pageCount"><spring:message code="book.addBook.pageCount"/><span class="red-text">*</span></form:label>
                <form:input type="number" path="pageCount" min="0"/>
            </div>
            <form:errors path="pageCount" cssClass="red-text"  element="p"/>
            <br>
            <div class="input-field">
                <form:label path="suggestedAge"><spring:message code="book.addBook.recommendedAge"/><span class="red-text">*</span></form:label>
                <form:input type="number" path="suggestedAge" min="0"/>
            </div>
            <form:errors path="suggestedAge" cssClass="red-text"  element="p"/>
            <br>
            <div class="input-field">
                <form:label path="price"><spring:message code="book.addBook.price"/><span class="red-text">*</span></form:label>
                <form:input type="number" path="price" step=".01" min="0"/>
            </div>
            <form:errors path="price" cssClass="red-text"  element="p"/>
            <br>
            <div class="input-field">
                <form:label path="cover" cssClass="active"><spring:message code="book.addBook.cover"/><span class="red-text">*</span> (.png, .jpeg)</form:label>
                <form:input type="file" path="cover" accept="image/*"/>
            </div>
            <form:errors path="cover" cssClass="red-text"  element="p"/>
            <br>
            <div class="input-field">
                <form:label path="preview" cssClass="active"><spring:message code="book.addBook.preview"/><span class="red-text">*</span> (.pdf)</form:label>
                <form:input type="file" path="preview" accept=".pdf"/>
            </div>
            <form:errors path="preview" cssClass="red-text"  element="p"/>
            <br>
            <div class="input-field">
                <form:label path="bookFile" cssClass="active"><spring:message code="book.addBook.bookFile"/><span class="red-text">*</span> (.pdf)</form:label>
                <form:input type="file" path="bookFile" accept=".pdf"/>
            </div>
            <form:errors path="bookFile" cssClass="red-text"  element="p"/>
            <br>
            <div class="input-field">
                <button class="btn waves-effect waves-light" type="submit" name="action">
                    <spring:message code="book.addBook.publish"/>
                </button>
            </div>
        </form:form>
    </div>
</div>
<script>

    document.addEventListener('DOMContentLoaded', function () {
        var elems = document.querySelectorAll('select');
        var instances = M.FormSelect.init(elems);
    });
</script>
</body>
</html>
