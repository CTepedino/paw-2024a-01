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

    <link rel="stylesheet" href="<c:url value="/css/topBarStyle.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/style.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/searchBar.css"/>"/>
</head>
<body>

<%@include file="materializeComponent.jsp"%>

<c:url value="/search" var="searchUrl"/>
<header>
    <nav>
        <div class="nav-wrapper row">
            <div class="logo-box col s4">
                <a href="<c:url value="/"/>">
                    <img class="logo" src="<c:url value="/images/cybrary_3.png"/>" alt="Cybrary"/>
                </a>
            </div>

            <div class="search-bar-box col s4 ">
                <c:if test="${!hideSearchBar}">
                    <form action="${searchUrl}" id="searchForm" class="hide-on-small-only">
                        <div class="search">
                            <a href="#" id="submitSearch">
                                <i class="search-icon material-icons black-text">search</i>
                            </a>
                            <input class="search-input browser-default" name="title" id="title" required autocomplete="off">
                        </div>
                    </form>
                </c:if>
            </div>

            <div class="options-box col s4">
                <c:if test="${!hideRightBar}">
                    <c:if test="${!isLoggedIn}">
                        <ul id="nav-mobile" class="right ">
                            <li>
                                <a href="<c:url value="/login"/>" class="waves-effect btn white-text">
                                    <spring:message code="session.login"/>
                                </a>
                            </li>
                            <li>
                                <a href="<c:url value="signup"/>" class="waves-effect btn white-text hide-on-med-and-down">
                                    <spring:message code="session.signup"/>
                                </a>
                            </li>
                        </ul>
                    </c:if>
                    <c:if test="${isLoggedIn}">
                        <a href="#" onclick="toggleMenu()">
                            <div class="logged-user-name ">
                                <span class="name-text hide-on-med-and-down"><c:out value="${loggedUser.firstName} ${loggedUser.lastName}"/> </span>
                                <img src="<c:url value="${baseUrl}/profilePicture/${loggedUser.userId}"/>" alt="userprofile" class="user-picture"/>
                            </div>
                        </a>
                        <div class="sub-menu-wrap" id="subMenu">
                            <div class="sub-menu">
                                <a href="<c:url value="/profile/${loggedUser.userId}"/>" class="sub-menu-link">
                                    <i class="material-icons go-icon">person</i>
                                    <p><spring:message code="topBar.profile"/></p>
                                    <span class="go-arrow">></span>
                                </a>
                                <hr>
                                <a href="<c:url value="/purchases"/>" class="sub-menu-link">
                                    <i class="material-icons go-icon">shopping_basket</i>
                                    <p><spring:message code="topBar.purchases"/></p>
                                    <span class="go-arrow">></span>
                                </a>
                                <hr>
                                <c:if test="${isWriter}">
                                    <a href="<c:url value="/sales"/>" class="sub-menu-link">
                                        <i class="material-icons go-icon">insert_chart</i>
                                        <p><spring:message code="topBar.sales"/></p>
                                        <span class="go-arrow">></span>
                                    </a>
                                    <hr>
                                </c:if>
                                <c:if test="${isWriter}">
                                    <a href="<c:url value="/analytics"/>" class="sub-menu-link">
                                        <i class="material-icons go-icon">analytics</i>
                                        <p><spring:message code="profile.analytics"/></p>
                                        <span class="go-arrow">></span>
                                    </a>
                                    <hr>
                                </c:if>
                                <a href="<c:url value="/questions"/>" class="sub-menu-link">
                                    <i class="material-icons go-icon">question_answer</i>
                                    <p><spring:message code="topBar.questions"/></p>
                                    <span class="go-arrow">></span>
                                </a>
                                <hr>
                                <a href="<c:url value="/addBook"/>" class="sub-menu-link">
                                    <i class="material-icons go-icon">book</i>
                                    <p><spring:message code="topBar.publish"/></p>
                                    <span class="go-arrow">></span>
                                </a>
                                <hr>
                                <a href="<c:url value="/logout"/>" class="sub-menu-link">
                                    <i class="material-icons go-icon">exit_to_app</i>
                                    <p><spring:message code="topBar.signOut"/></p>
                                    <span class="go-arrow">></span>
                                </a>
                                <hr>
                            </div>
                        </div>
                    </c:if>
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
<c:if test="${!hideSearchBar}">
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            var submitButton = document.getElementById('submitSearch');
            var form = document.getElementById('searchForm');
            var input = document.getElementById('title');

            submitButton.addEventListener('click', function(event) {
                event.preventDefault();

                if (input.checkValidity()){
                    form.submit();
                } else {
                    input.focus();
                }

            });
        });
    </script>
</c:if>
</body>
</html>

