<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Purchases</title>
    <script src="https://kit.fontawesome.com/0f001c5d7a.js" crossorigin="anonymous"></script>
    <link href="${pageContext.request.contextPath}/css/sidebarPlus.css" rel="stylesheet"/>
</head>

<jsp:include page="components/topBar.jsp">
    <jsp:param name="hasWriterRole" value="${hasWriterRole}" />
</jsp:include>

<body>
<div class="main--content">
<%--    <nav>--%>
<%--        <div class="nav-wrapper">--%>
<%--            <a href="${pageContext.request.contextPath}/" class="brand-logo"> <img class="logo" src="${pageContext.request.contextPath}/images/cybrary_3.png"></a>--%>
<%--        </div>--%>
<%--        <style>--%>
<%--            <%@include file="/css/topBarStyle.css" %>--%>
<%--        </style>--%>
<%--        <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>--%>
<%--    </nav>--%>


    <div class="header-wrapper">
        <div class="header--title">
            <span>My</span>
            <h2>Purchases</h2>
        </div>
        <div class="user--info">
            <div class="search--box">
                <a href="${pageContext.request.contextPath}/" > <i class="fa-solid fa-house"></i></a>
            </div>
        </div>

    </div>

    <div class="tabular-wrapper">
        <h3 class="main--title">
            Order History
        </h3>
        <div class="table-container">
            <table>
                <thead>
                <tr>
                    <th>Date</th>
                    <th>Writer</th>
                    <th>Book's Title</th>
                    <th>Amount</th>
                    <th>Status</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td> 2024-05-01 </td>
                    <td> Mariano Blatt </td>
                    <td> Mi Juventud Unidad</td>
                    <td> $500 </td>
                    <td>Pending</td>
                    <td><button>Edit</button></td>
                </tr>
                <tr>
                    <td> 2024-05-01 </td>
                    <td> JK ROWLING </td>
                    <td> Harry Potter</td>
                    <td> $500 </td>
                    <td>Completed</td>
                    <td><button>Edit</button></td>
                </tr>
                </tbody>
                <tfoot>

                </tfoot>
            </table>
        </div>
    </div>

</div>
</div>
</body>
</html>
