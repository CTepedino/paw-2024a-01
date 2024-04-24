<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="${pageContext.request.contextPath}/css/topBarStyle2.0.css" rel="stylesheet"/>
    <!-- Font Awesome CDN link-->
    <script src="https://kit.fontawesome.com/0f001c5d7a.js" crossorigin="anonymous"></script>
</head>
<body>
<div class="hero">
    <nav>
        <img src="images/cybrary_3.png" alt="logo" class="logo">


        <a class="user-pic" href="#">
            <i class="fas fa-user" onclick="toggleMenu()"></i>
        </a>

        <div class="sub-menu-wrap" id="subMenu">
            <div class="sub-menu">
                <a #href="#" class="sub-menu-link">
                    <i class="fas fa-user"></i>
                    <p>My Profile</p>
                    <span>></span>
                </a>
                <a #href="#" class="sub-menu-link">
                    <i class="fa-solid fa-bag-shopping"></i>
                    <p>Purchases</p>
                    <span>></span>
                </a>
                <a #href="#" class="sub-menu-link">
                    <i class="fas fa-chart-bar"></i>
                    <p>Sales</p>
                    <span>></span>
                </a>
                <a #href="#" class="sub-menu-link">
                    <i class="fa-solid fa-bag-shopping"></i>
                    <p>Publish</p>
                    <span>></span>
                </a>
                <a #href="#" class="sub-menu-link">
                    <i class="fas fa-sign-out-alt"></i>
                    <p>Logout</p>
                    <span>></span>
                </a>
            </div>

        </div>
    </nav>
</div>
<script>
    let subMenu = document.getElementById("subMenu");

    function toggleMenu(){
        subMenu.classList.toggle("open-menu")
    }
</script>
</body>
</html>
