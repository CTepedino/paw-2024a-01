
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
            <% for (int i = 0; i < 4; i++) { %>
            <div class="card">
                <div class="card-image waves-effect waves-block waves-light">
                    <img class="activator" src="/css/book-cover.jpg">
                </div>
                <div class="card-content">
                    <span class="card-title activator grey-text text-darken-4">Card Title <%= i %><i class="material-icons right">more_vert</i></span>
                    <p><a href="#">This is a link</a></p>
                </div>
                <div class="card-reveal">
                    <span class="card-title grey-text text-darken-4">Card Title <%= i %><i class="material-icons right">close</i></span>
                    <p>Here is some more information about this product that is only revealed once clicked on.</p>
                </div>
            </div>
            <% } %>
        </div>
    </div>
</body>
</html>
