<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
    <title>Title</title>
    <link href="/css/style.css" rel="stylesheet"/>
</head>
<body>
<%@ include file="components/header.jsp" %>
<h1>Hello ${user.username}</h1>
</body>
</html>
