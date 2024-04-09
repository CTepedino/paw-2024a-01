<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
    <title>Title</title>
    <link href="/css/bookInfo.css" rel="stylesheet"/>
</head>
<body>
    <%@ include file="components/topBar.jsp" %>
    <div class="container">
        <img class="book_cover" src="/css/book-cover.jpg">
        <div>
            <h2>${book.title}</h2>
            <h5>${book.writerName} ${book.writerLastName}</h5>
            <h5>${book.price}</h5>
            <h5>${book.suggestedAge}</h5>
            <h5>${book.genre}</h5>
            <h5>${book.pageCount}</h5>
        </div>
    </div>
    <p>${book.description}</p>
    <a class="waves-effect waves-light btn" href="/buy?writerEmail=${book.writerEmail}&bookTitle=${book.title}">Contact Writer</a>
</body>
</html>
