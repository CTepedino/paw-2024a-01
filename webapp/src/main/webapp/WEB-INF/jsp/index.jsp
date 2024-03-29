<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<html>
<body>
<%@ include file="components/header.jsp" %>
<h2>Hello World!</h2>
</body>
</html>

<%--%>
<html>
    <head>
        <title>PAW</title>
        <link href="/css/style.css" rel="stylesheet"/>
    </head>

    <body>
        <h2>Hello <c:out value="${user.username}"/>!</h2>
    </body>
</html>
</%--%>