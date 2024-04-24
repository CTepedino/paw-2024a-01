<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
    <meta charset="UTF-8">
    <title>Publish</title>
    <link href="${pageContext.request.contextPath}/css/addBook.css" rel="stylesheet"/>
</head>
<jsp:include page="components/topBar.jsp">
    <jsp:param name="hasWriterRole" value="${hasWriterRole}" />
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
                <h6><spring:message code="book.addBook.writerTitle"/></h6>
                <div class="input-field">
                    <form:label path="writerFirstName"><spring:message code="book.addBook.writerFirstName"/><span class="red-text">*</span></form:label><br>
                    <form:input type="text" path="writerFirstName"/>
                    <form:errors path="writerFirstName" element="p"/>
                </div>
                <div class="input-field">
                    <form:label path="writerLastName"><spring:message code="book.addBook.writerLastName"/><span class="red-text">*</span></form:label><br>
                    <form:input type="text" path="writerLastName"/>
                    <form:errors path="writerLastName" element="p"/>
                </div>
            </c:if>

            <h6><spring:message code="book.addBook.bookTitle"/></h6>

            <div class="input-field">
                <form:label path="title"><spring:message code="book.addBook.title"/><span class="red-text">*</span></form:label><br>
                <form:input type="text" path="title"/>
                <form:errors path="title" element="p"/>
            </div>
            <div class="input-field">
                <form:label path="description"><spring:message code="book.addBook.description"/><span class="red-text">*</span></form:label><br>
                <form:input type="text" path="description"/>
                <form:errors path="description" element="p"/>
            </div>
            <div class="input-field">
                <form:label path="genre" cssClass="active"><spring:message code="book.addBook.genre"/><span class="red-text">*</span></form:label><br>
                <form:select path="genre">
                    <form:option value="" disabled="true"> <spring:message code="book.addBook.genreTitle"/> </form:option>
                    <c:forEach items="${genres}" var="genre">
                        <form:option value="${genre}"><spring:message code="book.genre.${genre}"/></form:option>
                    </c:forEach>
                </form:select>
                <form:errors path="genre" element="p"/>
            </div>
            <div class="input-field">
                <form:label path="pageCount"><spring:message code="book.addBook.pageCount"/><span class="red-text">*</span></form:label><br>
                <form:input type="number" path="pageCount"/>
                <form:errors path="pageCount" element="p"/>
            </div>
            <div class="input-field">
                <form:label path="suggestedAge"><spring:message code="book.addBook.recommendedAge"/><span class="red-text">*</span></form:label><br>
                <form:input type="number" path="suggestedAge"/>
                <form:errors path="suggestedAge" element="p"/>
            </div>
            <div class="input-field">
                <form:label path="price"><spring:message code="book.addBook.price"/><span class="red-text">*</span></form:label><br>
                <form:input type="number" path="price" step="0.01"/>
                <form:errors path="price" element="p"/>
            </div>
            <div class="input-field">
                <form:label path="image" cssClass="active"><spring:message code="book.addBook.image"/><span class="red-text">*</span></form:label><br>
                <form:input type="file" path="image" accept=".png, .jpeg"/>
                <form:errors path="image" element="p"/>
            </div>
            <div class="input-field">
                <form:label path="pdf" cssClass="active"><spring:message code="book.addBook.pdf"/><span class="red-text">*</span></form:label><br>
                <form:input type="file" path="pdf" accept=".pdf"/>
                <form:errors path="pdf" element="p"/>
            </div>

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
