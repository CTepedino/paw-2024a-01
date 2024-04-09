<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:eval expression="@environment.getProperty('base.url')" var="baseUrl"/>
<html>
<head>
    <title>Title</title>
</head>
<body>
<object
    type="application/pdf"
    data="${baseUrl}/pdf/${pdfId}"
    width="600"
    height="700"
>
</object>
</body>
</html>
