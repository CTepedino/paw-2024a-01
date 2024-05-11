<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="preconnect" href="https://fonts.googleapis.com"/>
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin/>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Inter:wght@100..900&family=Roboto:ital,wght@1,100&display=swap"/>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css"/>

    <script src="https://kit.fontawesome.com/0f001c5d7a.js" crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">

    <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/images/cybrary.png"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/topBarStyle3.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/style.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/searchBar.css"/>"/>
</head>
<body>
<c:url value="/search" var="searchUrl"/>
<header>
    <nav>
        <div class="nav-wrapper row">
            <div class="logo-box col s4">
                <a href="<c:url value="/"/>">
                    <img class="logo" src="<c:url value="/images/cybrary_3.png"/>" alt="Cybrary"/>
                </a>
            </div>

            <div class="search-bar-box col s4 hide-on-med-and-down">
                <c:if test="${!param.hideSearchBar}">
                    <form action="${searchUrl}">
                        <div class="search">
                            <i class="search-icon material-icons black-text">search</i>
                            <input class="search-input browser-default" name="title" id="title" required placeholder="Search for a book">
                        </div>
                    </form>
                </c:if>
            </div>

            <div class="options-box col s4">
                <c:if test="${!isLoggedIn}">
                    <ul id="nav-mobile" class="right hide-on-med-and-down">
                        <li>
                            <a href="${pageContext.request.contextPath}/login" class="waves-effect btn white-text">
                                <spring:message code="session.login"/>
                            </a>
                        </li>
                        <li>
                            <a href="${pageContext.request.contextPath}/signup" class="waves-effect btn white-text">
                                <spring:message code="session.signup"/>
                            </a>
                        </li>
                    </ul>
                </c:if>
                <c:if test="${isLoggedIn}">

<%--                    <a class="dropdown-trigger" href="#" data-target="user-dropdown">
                        <div class="logged-user-name">
                            <span class="name-text">${loggedUser.firstName} ${loggedUser.lastName}</span>
                            <img src="<c:url value="${baseUrl}/profilePicture/${loggedUser.userId}"/>" alt="userprofile" class="user-picture"/>
                        </div>
                    </a>--%>
                    <a href="#" onclick="toggleMenu()">
                        <div class="logged-user-name">
                            <span class="name-text">${loggedUser.firstName} ${loggedUser.lastName}</span>
                            <img src="<c:url value="${baseUrl}/profilePicture/${loggedUser.userId}"/>" alt="userprofile" class="user-picture"/>
                        </div>
                    </a>
                    <div class="sub-menu-wrap" id="subMenu">
                        <div class="sub-menu">
                            <a href="<c:url value="/profile/${loggedUser.userId}"/>" class="sub-menu-link">
                                <i class="material-icons go-icon">person</i>
                                <p>My profile</p>
                                <span class="go-arrow">></span>
                            </a>
                            <hr>
                            <a href="<c:url value="/purchases"/>" class="sub-menu-link">
                                <i class="material-icons go-icon">shopping_basket</i>
                                <p>Purchases</p>
                                <span class="go-arrow">></span>
                            </a>
                            <hr>
                            <c:if test="${isWriter}">
                                <a href="<c:url value="/sales"/>" class="sub-menu-link">
                                    <i class="material-icons go-icon">insert_chart</i>
                                    <p>Sales</p>
                                    <span class="go-arrow">></span>
                                </a>
                                <hr>
                            </c:if>
                            <a href="<c:url value="/addBook"/>" class="sub-menu-link">
                                <i class="material-icons go-icon">book</i>
                                <p>Publish</p>
                                <span class="go-arrow">></span>
                            </a>
                            <hr>
                            <a href="<c:url value="/logout"/>" class="sub-menu-link">
                                <i class="material-icons go-icon">exit_to_app</i>
                                <p>Logout</p>
                                <span class="go-arrow">></span>
                            </a>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
    </nav>
</header>
<script>
    let subMenu = document.getElementById("subMenu");

    function toggleMenu(){
        subMenu.classList.toggle("open-menu")
    }

</script>
</body>
</html>


<%--                    <ul id='user-dropdown' class='dropdown-content'>
                        <li><a href="<c:url value="/profile"/>"><spring:message code="topBar.profile"/></a></li>
                        <li><a href="<c:url value="/purchases"/>"><spring:message code="topBar.purchases"/></a></li>
                        <li><a href="<c:url value="/addBook"/>><spring:message code="topBar.publish"/></a></li>
                        <c:if test="${param.hasWriterRole}">
                            <li><a href="<c:url value="/sales"/>"><spring:message code="topBar.sales"/></a></li>
                        <li><a href="<c:url value="/myBooks"/>"><spring:message code="topBar.myBooks"/></a></li>
                        </c:if>
                        <li><a href="<c:url value="/logout"/>" class="red-text"><spring:message code="topBar.signOut"/></a></li>
                    </ul>--%>

<%--                  <a class="row" href="#" onclick="toggleMenu()">
                      <div class="logged-user-name">${loggedUser.firstName} ${loggedUser.lastName}</div><i class="fas fa-user user-pic col s4"></i>
                  </a>
                  --%>
<%-- <div class="sub-menu-wrap" id="subMenu">
     <div class="sub-menu">


         <div class="user-info">
             <img src="<c:url value="${baseUrl}/profilePicture/${loggedUser.userId}"/>" alt="userprofile"/>
                 &lt;%&ndash;                    <img src="${pageContext.request.contextPath}/images/user.png"/>&ndash;%&gt;
             <a href="${pageContext.request.contextPath}/profile"> <h3>My profile</h3> </a>
         </div>
         <hr>
         <a href="${pageContext.request.contextPath}/purchases" class="sub-menu-link">
             <i class="fa-solid fa-bag-shopping"></i>
             <p>Purchases</p>
             <span>></span>
         </a>

         <c:if test="${param.hasWriterRole}">
             <a href="${pageContext.request.contextPath}/sales" class="sub-menu-link">
                 <i class="fas fa-chart-bar"></i>
                 <p>Sales</p>
                 <span>></span>
             </a>
         </c:if>

         <a href="${pageContext.request.contextPath}/addBook" class="sub-menu-link">
             <i class="fa-solid fa-bag-shopping"></i>
             <p>Publish</p>
             <span>></span>
         </a>

         <a href="${pageContext.request.contextPath}/logout" class="sub-menu-link">
             <i class="fas fa-sign-out-alt"></i>
             <p>Logout</p>
             <span>></span>
         </a>

     </div>

 </div>--%>