<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><spring:message code="book.editBook"/></title>
    <link href="<c:url value="/css/addBook.css"/>" rel="stylesheet"/>
</head>

<c:set value="${true}" scope="request" var="hideSearchBar"/>
<%@include file="components/topBar.jsp" %>

<body>
<div class="container">
    <div class="form">

        <c:url value="/book/edit/${id}" var="postUrl"/>
        <form:form
                modelAttribute="editBookForm"
                action="${postUrl}"
                method="post"
                enctype="multipart/form-data"
                cssClass="z-depth-2"
        >

            <h5 class="publish-title"><spring:message code="book.editBook.title"/></h5>

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
            <div class="file-field input-field">
                <div class="btn">
                    <span><spring:message code="book.addBook.cover"/></span>
                    <input type="file" accept="image/*" name="cover" id="cover">
                </div>
                <div class="file-path-wrapper">
                    <input class="file-path validate" type="text" \>
                </div>
            </div>
            <form:errors path="cover" cssClass="red-text"  element="p"/>


            <div class="file-field input-field">
                <div class="btn">
                    <span><spring:message code="book.addBook.preview"/></span>
                    <input type="file" accept="application/pdf" name="preview" id="preview">
                </div>
                <div class="file-path-wrapper">
                    <input class="file-path validate" type="text" \>
                </div>
            </div>
            <form:errors path="preview" cssClass="red-text"  element="p"/>

            <div class="file-field input-field">
                <div class="btn">
                    <span><spring:message code="book.addBook.bookFile"/></span>
                    <input type="file" accept="application/pdf" name="bookFile" id="bookFile">
                </div>
                <div class="file-path-wrapper">
                    <input class="file-path validate" type="text" \>
                </div>
            </div>
            <form:errors path="bookFile" cssClass="red-text"  element="p"/>


            <div class="input-field center">
                <button class="btn waves-effect waves-light" type="submit" name="action">
                    <strong><spring:message code="user.profile.edit.save"/></strong>
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
