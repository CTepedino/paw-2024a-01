<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="${pageContext.request.contextPath}/css/topBarStyle2.0.css" rel="stylesheet"/>
    <!-- Font Awesome CDN link-->
    <script src="https://kit.fontawesome.com/0f001c5d7a.js" crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
</head>
<header class="no-autoinit">
<div>
    <nav>

        <a href="${pageContext.request.contextPath}/"> <img class="logo" src="${pageContext.request.contextPath}/images/cybrary_3.png"></a>

        <div>
            <c:if test="${empty pageContext.request.userPrincipal}">
                <a href="${pageContext.request.contextPath}/login" class="waves-effect btn white-text">
                    <spring:message code="session.login"/>
                </a>
                <a href="${pageContext.request.contextPath}/signup" class="waves-effect btn white-text">
                    <spring:message code="session.signup"/>
                </a>
            </c:if>
        </div>



        <c:if test="${not empty pageContext.request.userPrincipal}">

            <a class="user-pic" href="#">
                <i class="fas fa-user" onclick="toggleMenu()"></i>
            </a>

            <div class="sub-menu-wrap" id="subMenu">
            <div class="sub-menu">


                <div class="user-info">
                    <img src="<c:url value="${baseUrl}/profilePicture/${user.userId}"/>" alt="userprofile"/>
<%--                    <img src="${pageContext.request.contextPath}/images/user.png"/>--%>
                    <a href="${pageContext.request.contextPath}/profile"> <h3>My profile</h3> </a>
                </div>
                <hr>

              <%--  <a href="${pageContext.request.contextPath}/profile" class="sub-menu-link">
                    <i class="fas fa-user"></i>
                    <p>My Profile</p>
                    <span>></span>
                </a>--%>
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

        </div>
        </c:if>
    </nav>
</div>
<script>
    let subMenu = document.getElementById("subMenu");

    function toggleMenu(){
        subMenu.classList.toggle("open-menu")
    }
</script>
</header>
</html>
