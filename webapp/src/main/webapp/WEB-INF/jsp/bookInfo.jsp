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
        <img class="activator" src="/css/book-cover.jpg">
        <div>
            <h2>${book.title}</h2>
            <h5>${book.writerName} ${book.writerSurname}</h5>
            <h5>${book.price}</h5>
            <h5>${book.suggestedAge}</h5>
            <h5>${book.genra}</h5>
            <h5>${book.pageNumbers}</h5>
        </div>
    </div>
    <p>${book.description}</p>
    <button>Contact Writer</button>
</body>
</html>
