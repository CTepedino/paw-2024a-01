<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:eval expression="@environment.getProperty('base.url')" var="baseUrl"/>
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
            <table class="my-table">
                <thead>
                <tr class="my-tr">
                    <th class="my-th">Writer</th>
                    <th class="my-th">Email</th>
                    <th class="my-th">Book's Title</th>
                    <th class="my-th">Price</th>
                    <th class="my-th">Status</th>
                    <th class="my-th">Action</th>
                </tr>
                </thead>
                <tbody>
<%--                <c:forEach var="order" items="${order}">--%>
<%--                    <c:set var="order" value="${order}" scope="request"/>--%>
<%--                    --%>
<%--                </c:forEach>--%>
                <c:forEach var="order" items="${orders}">
                <tr class="my-tr">
                        <td class="my-td"><c:out value="${order.writer.firstName} ${order.writer.lastName}"/></td>
                        <td class="my-td"><c:out value="${order.writer.email}"/></td>
                        <td class="my-td"><c:out value="${order.book.title}"/></td>
                        <td class="my-td"><c:out value="${order.book.price}"/></td>
                        <td class="my-td"><c:out value="${order.orderStatus.displayString}"/></td>
                        <c:url value="/advanceOrder" var="advanceOrderUrl">
                            <c:param name="bookId" value="${order.book.bookId}"/>
                            <c:param name="buyerId" value="${order.buyer.id}"/>
                            <c:param name="writerId" value="${order.writer.id}"/>
                            <c:param name="from" value="purchases"/>
                        </c:url>

                        <c:if test="${order.orderStatus.readerCanAdvance}">
                        <td class="my-td"><form action="${advanceOrderUrl}" method="post">
                            <button
                                type="submit"
                            >Advance</button>
                        </form></td>
                        </c:if>
                        <c:if test="${!order.orderStatus.readerCanAdvance}">
                            <td class="my-td"><form action="${advanceOrderUrl}" method="post">
                                <button type="submit" disabled>Advance</button>
                            </form></td>
                        </c:if>
                </tr>
                </c:forEach>
<%--                <tr>
                    <td> 2024-05-01 </td>
                    <td> JK ROWLING </td>
                    <td> Harry Potter</td>
                    <td> $500 </td>
                    <td>Completed</td>
                    <td><button>Edit</button></td>
                </tr>--%>
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
