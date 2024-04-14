<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Confirmation</title>
    <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/images/cybrary.png" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/emailConfirmation.css">
</head>

<body>
    <div class="container">
        <div class="card-panel teal lighten-2 white-text">
            <h4>Your information has been sent!</h4>
            <p>Await for further instructions in your inbox.</p>
        </div>
        <a class="waves-effect waves-light btn" href="${pageContext.request.contextPath}/">Return Home</a>
    </div>
</body>
</html>