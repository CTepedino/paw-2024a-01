<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<html>
<head>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@100..900&family=Roboto:ital,wght@1,100&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">



    <link href="${pageContext.request.contextPath}/css/topBarStyle.css" rel="stylesheet"/>
    <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/images/cybrary.png" />
</head>
<body>
<c:url value="/search" var="searchUrl"/>
<header>
    <nav>
        <div class="nav-wrapper">
            <a href="${pageContext.request.contextPath}/" class="brand-logo"> <img class="logo" src="${pageContext.request.contextPath}/images/cybrary_3.png"></a>

            <div class="nav-wrapper row">
                <div class="col s4">
                    <a href="${pageContext.request.contextPath}/" class="brand-logo left">
                        <img class="logo" alt="Cybrary logo" src="${pageContext.request.contextPath}/images/cybrary_3.png">
                    </a>
                </div>

                <div class="col s4">
                    <c:if test="${not empty pageContext.request.userPrincipal}">
                        <form action="${searchUrl}" <%--class="col s4"--%>>
                              <div class="input-field">
                                <input id="title" name="title" type="search" required>
                                <label class="label-icon" for="title"><i class="material-icons">search</i></label>
                                <i class="material-icons">close</i>
                            </div>
                        </form>
                    </c:if>
                </div>

                <div class="col s4">
                    <ul id="nav-mobile" class="right hide-on-med-and-down">
                        <li>
                            <c:if test="${not empty pageContext.request.userPrincipal}">
                                <a class='dropdown-trigger btn' href='#' data-target='user-dropdown'>
                                    <i class="material-icons">account_circle</i>
                                </a>

                                <ul id='user-dropdown' class='dropdown-content'>
                                    <li><a href="${pageContext.request.contextPath}/profile">Profile</a></li>
                                    <li><a href="${pageContext.request.contextPath}/purchases">Purchases</a></li>
                                    <li><a href="${pageContext.request.contextPath}/addBook">Publish</a></li>
                                    <c:if test="${param.hasWriterRole}">
                                        <li><a href="${pageContext.request.contextPath}/sales">Sales</a></li>
                                    </c:if>
                                    <li><a href="${pageContext.request.contextPath}/logout" class="red-text">Sign Out</a></li>
                                </ul>
                            </c:if>
                            <c:if test="${empty pageContext.request.userPrincipal}">
                                <a href="${pageContext.request.contextPath}/login" class="waves-effect btn white-text">Log In</a>
                                <a href="${pageContext.request.contextPath}/signup" class="waves-effect btn white-text">Sign Up</a>
                            </c:if>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </nav>
</header>


<!-- Compiled and minified JavaScript -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
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
