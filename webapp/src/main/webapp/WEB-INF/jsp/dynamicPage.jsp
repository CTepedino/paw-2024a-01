<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${title}</title>
</head>

<body>
<%@ include file="components/topBar.jsp" %>
    <h1>${title}</h1>
    <p>Username: ${user.username}</p>
</body>

<script>
    if (${user.userId > 1}) {
        alert("id is bigger than 1");
    }
</script>
</html>
