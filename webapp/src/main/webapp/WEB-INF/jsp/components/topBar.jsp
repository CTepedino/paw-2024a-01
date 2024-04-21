<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@100..900&family=Roboto:ital,wght@1,100&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
    <!-- Compiled and minified JavaScript -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
    <link href="${pageContext.request.contextPath}/css/topBarStyle.css" rel="stylesheet"/>
    <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/images/cybrary_3.png" />
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
</head>
<body>
<header>
    <nav>
        <div class="nav-wrapper">
            <a href="${pageContext.request.contextPath}/" class="brand-logo"> <img class="logo" src="${pageContext.request.contextPath}/images/cybrary_3.png"></a>
            <ul id="nav-mobile" class="right hide-on-med-and-down">
                <li><a href="${pageContext.request.contextPath}/addBook" class="waves-effect btn">Publish</a></li>
                <li>
                <c:if test="true">
                    <a class='dropdown-trigger btn' href='#' data-target='user-dropdown'>
                        <i class="material-icons">account_circle</i>
                    </a>

                    <ul id='user-dropdown' class='dropdown-content'>
                        <li><a href="#">Account</a></li>
                        <li><a href="#">My Orders</a></li>
                        <c:if test="true">
                            <li><a href="#">My Books</a></li>
                        </c:if>
                        <li><a href="/logout" class="red-text">Sign Out</a></li>
                    </ul>
                </c:if>
                <c:if test="true">
                    <a href="${pageContext.request.contextPath}/login" class="waves-effect btn">Sign In</a>
                </c:if>
                </li>
            </ul>
        </div>
    </nav>
</header>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        var elems = document.querySelectorAll('.dropdown-trigger');
        var options = {
            constrainWidth: false,
            coverTrigger: false
        };
        var instances = M.Dropdown.init(elems, options);
    });
</script>

</body>
</html>
