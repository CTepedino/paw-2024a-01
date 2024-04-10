<%--suppress JSUnresolvedLibraryURL --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Publish Book</title>
    <link href="${pageContext.request.contextPath}/css/addBook.css" rel="stylesheet"/>
</head>
<body>
<%@ include file="components/topBar.jsp" %>
<c:url value="/addBook" var="postUrl"/>
<div class="container">
<div class="form">
    <h5>Publish your Book!</h5>
    <form class="z-depth-2" action="${postUrl}" method="post" enctype="multipart/form-data" >
        <h6>Fill in your details</h6>

        <div>
            <div class="input-field">
                <label for="writerFirstName">Author's name<span class="red-text">*</span></label><br>
                <input type="text" id="writerFirstName" name="writerFirstName" class="validate">
                <span class="helper-text" data-error="Please enter the author's name"></span>
            </div>
        </div>
        <div>
            <div class="input-field">
                <label for="writerLastName">Author's surname<span class="red-text">*</span></label><br>
                <input type="text" id="writerLastName" name="writerLastName" class="validate">
                <span class="helper-text" data-error="Please enter the author's surname"></span>
            </div>
        </div>
        <div>
            <div class="input-field">
                <label for="writerEmail">Contact email<span class="red-text">*</span></label><br>
                <input type="text" id="writerEmail" name="writerEmail" class="validate">
                <span class="helper-text" data-error="Please enter a valid email"></span>
            </div>
        </div>

        <h6>Fill in the book's details</h6>

        <div>
            <div class="input-field">
                <label for="title">Title<span class="red-text">*</span></label><br>
                <input type="text" id="title" name="title" class="validate">
                <span class="helper-text" data-error="Please complete the book's title"></span>
            </div>
        </div>
        <div>
            <div class="input-field">
                <label for="description">Description<span class="red-text">*</span></label><br>
                <input type="text" id="description" name="description" class="materialize-textarea" placeholder="Short description of your book"></input>
                <span class="helper-text" data-error="Please enter a description"></span>
            </div>
        </div>
        <div>
            <div class="input-field">
                <label for="genre" class="active">Genre<span class="red-text">*</span></label>
                <select name="genre" id="genre"  >
                    <option value="" disabled> Select a genre </option>
                    <c:forEach items="${genres}" var="genre">
                        <option value="${genre}"><c:out value="${genre.displayName}"/></option>
                    </c:forEach>
                </select>
                <span class="helper-text" data-error="Please select your book's genre"></span>
            </div>
        </div>
        <div>
            <div class="input-field">
                <label for="pageCount">Page count<span class="red-text">*</span></label><br>
                <input id="pageCount" name="pageCount" class="validate">
                <span class="helper-text" data-error="Please enter the number of pages"></span>
            </div>
        </div>
        <div>
                <div class="input-field">
                    <label for="suggestedAge">Recommended age<span class="red-text">*</span></label><br>
                    <input type="number" id="suggestedAge" name="suggestedAge" class="validate" >
                    <span class="helper-text" data-error="Please enter recommended age"></span>
                </div>
        </div>
        <div>
            <div class="input-field">
                <label for="price">Price<span class="red-text">*</span></label><br>
                <input type="number" id="price" name="price">
                <span class="helper-text" data-error="Please enter the price of the book"></span>
            </div>
        </div>
        <div>
            <div class="input-field">
                <label for="image" class="active">Cover image<span class="red-text">*</span></label><br>
                <input type="file" id="image" name="image" accept=".png, .jpeg">
                <span class="helper-text" ></span>
            </div>
        </div>
        <div>
            <div class="input-field">
                <label for="pdf" class="active">Book's preview<span class="red-text">*</span></label><br>
                <input type="file" id="pdf" name="pdf"  accept=".pdf">
                <span class="helper-text" ></span>
            </div>
        </div>
        <div>
            <div class="input-field">
                <button class="btn waves-effect waves-light" type="submit" name="action">
                    Publish
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
