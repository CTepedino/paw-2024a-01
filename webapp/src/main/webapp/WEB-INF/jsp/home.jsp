
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
    <title>Home</title>
    <link href="/css/home.css" rel="stylesheet"/>
</head>
<body>
    <%@ include file="components/topBar.jsp" %>
    <div class="explore_back">
        <div class="container">
            <h5 class="explore_books">Find New Books</h5>
            <div>
                <h6 class="steps">1. Select your favorite book</h6>
                <h6 class="steps">2. Share your information with the author</h6>
                <h6 class="steps">3. Arrange delivery</h6>
            </div>
        </div>
    </div>
    <div class="books">
        <div class="container">
        <c:forEach var="book" items="${books}">
            <div class="card" >
                <a href="${pageContext.request.contextPath}/${book.bookId}">
                <div class="card-image waves-effect waves-block waves-light">
                    <img class="activator" src="/css/book-cover.jpg">
                </div>
                </a>
                <a href="${pageContext.request.contextPath}/${book.bookId}">
                <div class="card-content" >
                    <span class="card-title grey-text text-darken-4">${book.title}</span>
                    <p>${book.writerName} ${book.writerLastName}</p>
                    <p>${book.genre}</p>
                    <p>+ ${book.suggestedAge}</p>
                    <p>$ ${book.price}</p>

                </div>
                </a>
            </div>
            </a>
        </c:forEach>
        </div>
    </div>
</body>
</html>
