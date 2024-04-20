<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@100..900&family=Roboto:ital,wght@1,100&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">

    <!-- Compiled and minified JavaScript -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>

    <link href="${pageContext.request.contextPath}/css/topBarStyle.css" rel="stylesheet"/>
    <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/images/cybrary.png" />
</head>
<body>
<c:url value="/search" var="searchUrl"/>
<header>
    <nav>
        <div class="nav-wrapper row">
            <div class="col s4">
                <a href="${pageContext.request.contextPath}/" class="brand-logo left">
                    <img class="logo" alt="Cybrary logo" src="${pageContext.request.contextPath}/images/cybrary_3.png">
                </a>
            </div>

            <form action="${searchUrl}" class="col s4">
                  <div class="input-field">
                    <input id="title" type="search" required>
                    <label class="label-icon" for="title"><i class="material-icons">search</i></label>
                    <i class="material-icons">close</i>
                </div>
            </form>

            <div class="col s4">
                <ul id="nav-mobile" class="right hide-on-small-and-down">
                    <li><a href="${pageContext.request.contextPath}/addBook" class="waves-effect btn">Publish</a></li>
                </ul>
            </div>
        </div>
    </nav>
</header>
</body>
</html>
