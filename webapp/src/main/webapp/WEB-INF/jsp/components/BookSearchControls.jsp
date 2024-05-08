<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<spring:eval expression="@environment.getProperty('base.url')" var="baseUrl"/>
<html>
<head>
    <title>Search for a book</title>
</head>
<body>
<form:form>
    <div>
        <label>Title</label>
        <input type="text"/>
    </div>
    <div>
        <label>Genre</label>
        <select name="genre" id="genre"  >
            <option value="" disabled> Select a genre </option>
            <c:forEach items="${genres}" var="genre">
                <option value="${genre}"><c:out value="${genre.displayName}"/></option>
            </c:forEach>
        </select>
    </div>
    <div>
        <label>Minimum suggested age</label>
        <input type="number"/>
    </div>
    <div>
        <label>Order by</label>
        <select>
            <option value="" disabled>...</option>
            <option value="published_date">Date</option>
            <option value="page_count">Page Count</option>
            <option value="price">Price</option>
        </select>
    </div>
    <div>
        <label>Direction</label>
        <select>
            <option value="asc">Ascending</option>
            <option value="desc">Descending</option>
        </select>
    </div>
    <label>Writer</label>
</form:form>
</body>
</html>
