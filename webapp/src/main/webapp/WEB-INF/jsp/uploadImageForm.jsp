<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

Upload File :
<c:url value="/uploadImage" var="uploadFile"/>
<form name="fileUpload" method="POST" action="${uploadFile}" enctype="multipart/form-data">
    <label>Select File</label> <br />
    <input type="file" name="file" accept=".png, .jpeg"/>
    <input type="submit" name="submit" value="Upload" />
</form>
</body>
</html>
