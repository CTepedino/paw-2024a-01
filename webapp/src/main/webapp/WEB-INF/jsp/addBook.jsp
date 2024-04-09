<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Publicar Libro</title>
</head>
<body>
<h2>Publicar Nuevo Libro</h2>
<form action="${pageContext.request.contextPath}/addbook" method="post">
    <label for="title">Titulo:</label><br>
    <input type="text" id="title" name="title"><br>

  <%--  <label for="description">Descripcion:</label><br>
    <input type="text"  id="description" name="description"><br>

    <label for="genra">Genero:</label><br>
    <input type="text" id="genra" name="genra"><br>

    <label for="price">Precio:</label><br>
    <input type="number" id="price" name="price"><br>

    <label for="page_numbers">Numero de Paginas:</label><br>
    <input type="number" id="page_numbers" name="page_numbers"><br>

    <label for="prev">Previsualizacion:</label><br>
    <input type="text" id="prev" name="prev"><br>

    <label for="image">Imagen:</label><br>
    <input type="text" id="image" name="image"><br>

    <label for="suggested_age">Edad Sugerida:</label><br>
    <input type="number" id="suggested_age" name="suggested_age"><br>

    <label for="published_date">Fecha de Publicación:</label><br>
    <input type="text" id="published_date" name="published_date"><br>

    <label for="writer_email">Ingrese su correo electrónico:</label><br>
    <input type="text" id="writer_email" name="writer_email"><br>--%>
    <input type="submit" value="Publicar">
</form>
</body>
</html>