<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
    <meta charset="UTF-8">
    <title>Publish</title>
    <link href="${pageContext.request.contextPath}/css/addBook.css" rel="stylesheet"/>
</head>
<body>
<%@ include file="components/topBar.jsp" %>
<div class="container">
    <div class="form">
        <h5>Publish your Book!</h5>
        <c:url value="/addBook" var="postUrl"/>
        <form:form
                modelAttribute="newBookForm"
                action="${postUrl}"
                method="post"
                enctype="multipart/form-data"
                cssClass="z-depth-2"
        >
            <h6>Fill in your details</h6>

            <div class="input-field">
                <form:label path="writerFirstName">Author's name<span class="red-text">*</span></form:label><br>
                <form:input type="text" path="writerFirstName"/>
                <form:errors path="writerFirstName" element="p"/>
            </div>
            <div class="input-field">
                <form:label path="writerLastName">Author's surname<span class="red-text">*</span></form:label><br>
                <form:input type="text" path="writerLastName"/>
                <form:errors path="writerLastName" element="p"/>
            </div>
            <div class="input-field">
                <form:label path="writerEmail">Contact email<span class="red-text">*</span></form:label><br>
                <form:input type="text" path="writerEmail"/>
                <form:errors path="writerEmail" element="p"/>
            </div>

            <h6>Fill in the book's details</h6>

            <div class="input-field">
                <form:label path="title">Title<span class="red-text">*</span></form:label><br>
                <form:input type="text" path="title"/>
                <form:errors path="title" element="p"/>
            </div>
            <div class="input-field">
                <form:label path="description">Description<span class="red-text">*</span></form:label><br>
                <form:input type="text" path="description"/>
                <form:errors path="description" element="p"/>
            </div>
            <div>
                <div class="input-field">
                    <form:label path="genre" cssClass="active">Genre<span class="red-text">*</span></form:label><br>
                    <form:select path="genre">
                        <form:option value="" disabled="true"> Select a genre </form:option>
                        <form:options items="${genres}" itemLabel="displayName"/>
                    </form:select>
                    <form:errors path="genre" element="p"/>
                </div>
            </div>

            <div class="input-field">
                <form:label path="pageCount">Page count<span class="red-text">*</span></form:label><br>
                <form:input type="number" path="pageCount"/>
                <form:errors path="pageCount" element="p"/>
            </div>
            <div class="input-field">
                <form:label path="suggestedAge">Suggested age<span class="red-text">*</span></form:label><br>
                <form:input type="number" path="suggestedAge"/>
                <form:errors path="suggestedAge" element="p"/>
            </div>
            <div class="input-field">
                <form:label path="price">Price<span class="red-text">*</span></form:label><br>
                <form:input type="number" path="price"/>
                <form:errors path="price" element="p"/>
            </div>
            <div class="input-field">
                <form:label path="image" cssClass="active">Cover image<span class="red-text">*</span></form:label><br>
                <form:input type="file" path="image" accept=".png, .jpeg"/>
                <form:errors path="image" element="p"/>
            </div>
            <div class="input-field">
                <form:label path="pdf" cssClass="active">Book's preview<span class="red-text">*</span></form:label><br>
                <form:input type="file" path="pdf" accept=".pdf"/>
                <form:errors path="pdf" element="p"/>
            </div>
            <div class="input-field">
                <button class="btn waves-effect waves-light" type="submit" name="action">
                    Publish
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
