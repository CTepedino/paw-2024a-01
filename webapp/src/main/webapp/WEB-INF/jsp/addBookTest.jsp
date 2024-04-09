<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
    <title>Publish Book</title>
</head>
<body>
<c:url value="/addBook" var="postUrl"/>
<form action="${postUrl}" method="post" enctype="multipart/form-data">

    <div>
        <input name="writerFirstName" placeholder="first name"/>
    </div>
    <div>
        <input name="writerLastName" placeholder="last name"/>
    </div>
    <div>
        <input name="writerEmail" placeholder="email"/>
    </div>
    <div>
        <input name="title" placeholder="title"/>
    </div>
    <div>
        <input name="description" placeholder="description"/>
    </div>
    <div>
        <input name="genre" placeholder="genre"/>
    </div>
    <div>
        <input name="suggestedAge" placeholder="suggested age"/>
    </div>
    <div>
        <input name="price" placeholder="price"/>
    </div>
    <div>
        <input name="pageCount" placeholder="page count"/>
    </div>
    <div>
        <input name="image" placeholder="image" type="file" accept=".png, .jpeg"/>
    </div>
    <div>
        <input name="pdf" placeholder="pdf" type="file" accept="application/pdf"/>
    </div>

    <div>
        <input type="submit" value="Publish!"/>
    </div>
</form>
</body>
</html>
