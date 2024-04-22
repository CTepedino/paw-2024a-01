<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<body>
<c:url value="/login" var="loginUrl" />

<form action="${loginUrl}" method="post">
    <div>
        <label for="email">Email: </label>
        <input id="email" name="email" type="text"/>
    </div>
    <div>
        <label for="password">Password: </label>
        <input id="password" name="password" type="password"/>
    </div>
    <div>
        <label><input name="rememberMe" type="checkbox"/> Remember Me</label>
    </div>
    <div>
        <input type="submit" value="Login!"/>
    </div>
</form>

</body>
</html>