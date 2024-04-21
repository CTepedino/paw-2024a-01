<%--suppress JSUnresolvedLibraryURL --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Publish</title>
    <link href="${pageContext.request.contextPath}/css/addBook.css" rel="stylesheet"/>
</head>
<%@ include file="components/topBar.jsp" %>
<body>
<c:url value="/addBook" var="postUrl"/>
<div class="container">
<div class="form">
    <h5><spring:message code="book.addBook.pageTitle"/></h5>
    <form class="z-depth-2" action="${postUrl}" method="post" enctype="multipart/form-data" >
        <h6><spring:message code="book.addBook.writerTitle"/></h6>

        <div>
            <div class="input-field">
                <label for="writerFirstName"><spring:message code="book.addBook.writerFirstName"/><span class="red-text">*</span></label><br>
                <input type="text" id="writerFirstName" name="writerFirstName" class="validate">
                <span class="helper-text" data-error="Please enter the author's name"></span>
            </div>
        </div>
        <div>
            <div class="input-field">
                <label for="writerLastName"><spring:message code="book.addBook.writerLastName"/><span class="red-text">*</span></label><br>
                <input type="text" id="writerLastName" name="writerLastName" class="validate">
                <span class="helper-text" data-error="Please enter the author's surname"></span>
            </div>
        </div>
        <div>
            <div class="input-field">
                <label for="writerEmail"><spring:message code="book.addBook.writerEmail"/><span class="red-text">*</span></label><br>
                <input type="text" id="writerEmail" name="writerEmail" class="validate">
                <span class="helper-text" data-error="Please enter a valid email"></span>
            </div>
        </div>

        <h6><spring:message code="book.addBook.bookTitle"/></h6>

        <div>
            <div class="input-field">
                <label for="title"><spring:message code="book.addBook.title"/><span class="red-text">*</span></label><br>
                <input type="text" id="title" name="title" class="validate">
                <span class="helper-text" data-error="Please complete the book's title"></span>
            </div>
        </div>
        <div>
            <div class="input-field">
                <label for="description"><spring:message code="book.addBook.description"/><span class="red-text">*</span></label><br>
                <input type="text" id="description" name="description" class="materialize-textarea" placeholder="Short description of your book"></input>
                <span class="helper-text" data-error="Please enter a description"></span>
            </div>
        </div>
        <div>
            <div class="input-field">
                <label for="genre" class="active"><spring:message code="book.addBook.genre"/><span class="red-text">*</span></label>
                <select name="genre" id="genre"  >
                    <option value="" disabled> <spring:message code="book.addBook.genreTitle"/></option>
                    <c:forEach items="${genres}" var="genre">
                        <option value="${genre}"><spring:message code="book.genre.${genre}"/></option>
                    </c:forEach>
                </select>
                <span class="helper-text" data-error="Please select your book's genre"></span>
            </div>
        </div>
        <div>
            <div class="input-field">
                <label for="pageCount"><spring:message code="book.addBook.pageCount"/><span class="red-text">*</span></label><br>
                <input id="pageCount" name="pageCount" class="validate">
                <span class="helper-text" data-error="Please enter the number of pages"></span>
            </div>
        </div>
        <div>
                <div class="input-field">
                    <label for="suggestedAge"><spring:message code="book.addBook.recommendedAge"/><span class="red-text">*</span></label><br>
                    <input type="number" id="suggestedAge" name="suggestedAge" class="validate" >
                    <span class="helper-text" data-error="Please enter recommended age"></span>
                </div>
        </div>
        <div>
            <div class="input-field">
                <label for="price"><spring:message code="book.addBook.price"/><span class="red-text">*</span></label><br>
                <input type="number" id="price" name="price">
                <span class="helper-text" data-error="Please enter the price of the book"></span>
            </div>
        </div>
        <div>
            <div class="input-field">
                <label for="image" class="active"><spring:message code="book.addBook.image"/><span class="red-text">*</span></label><br>
                <input type="file" id="image" name="image" accept=".png, .jpeg">
                <span class="helper-text" ></span>
            </div>
        </div>
        <div>
            <div class="input-field">
                <label for="pdf" class="active"><spring:message code="book.addBook.pdf"/><span class="red-text">*</span></label><br>
                <input type="file" id="pdf" name="pdf"  accept=".pdf">
                <span class="helper-text" ></span>
            </div>
        </div>
        <div>
            <div class="input-field">
                <button class="btn waves-effect waves-light" type="submit" name="action">
                    <spring:message code="book.addBook.publish"/>
                </button>
            </div>
        </div>
    </form>
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
