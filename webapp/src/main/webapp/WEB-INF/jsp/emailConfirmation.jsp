<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Email Confirmation</title>
</head>

<body>
    <script>
        alert("Your information has been sent to the seller!\nAwait for further instructions in your inbox.");
        window.location.href = "${pageContext.request.contextPath}/";
    </script>
</body>

</html>
