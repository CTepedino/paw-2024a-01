<%--suppress JSUnresolvedLibraryURL --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Publicar Libro</title>
    <link href="/css/style.css" rel="stylesheet"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
    <!-- Compiled and minified JavaScript -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>

</head>
<body>
<%@ include file="components/topBar.jsp" %>
<h5>¡Subí tu libro!</h5>
<c:url value="/addBook" var="postUrl"/>
<div class="row form">
    <form class="col s6 z-depth-2 center" action="${postUrl}" method="post" enctype="multipart/form-data" >
        <h4>Ingresá tus datos</h4>

        <div class="row">
            <div class="input-field col s12">
                <label for="writerFirstName">Nombre del autor<span class="red-text">*</span></label><br>
                <input type="text" id="writerFirstName" name="writerFirstName" class="validate" placeholder="Ejemplo: Gabriel García Márquez">
                <span class="helper-text" data-error="Por favor ingrese el nombre del autor"></span>
            </div>
        </div>
        <div class="row">
            <div class="input-field col s12">
                <label for="writerLastName">Apellido del autor<span class="red-text">*</span></label><br>
                <input type="text" id="writerLastName" name="writerLastName" class="validate" placeholder="Ejemplo: García Márquez">
                <span class="helper-text" data-error="Por favor ingrese el apellido del autor"></span>
            </div>
        </div>
        <div class="row">
            <div class="input-field col s12">
                <label for="writerEmail">Correo electrónico del autor<span class="red-text">*</span></label><br>
                <input type="text" id="writerEmail" name="writerEmail" class="validate" placeholder="Ejemplo: autor@example.com">
                <span class="helper-text" data-error="Por favor ingrese un correo electrónico válido"></span>
            </div>
        </div>

        <h4>Ingresá los datos del libro</h4>

        <div class="row">
            <div class="input-field col s12">
                <label for="title">Título del libro<span class="red-text">*</span></label><br>
                <input type="text" id="title" name="title" class="validate" placeholder="Ejemplo: Cien años de soledad">
                <span class="helper-text" data-error="Por favor ingrese el título del libro"></span>
            </div>
        </div>
        <div class="row">
            <div class="input-field col s12">
                <label for="description">Descripción del libro<span class="red-text">*</span></label><br>
                <input type="text" id="description" name="description" class="materialize-textarea" placeholder="Breve descripción del contenido del libro"></input>
                <span class="helper-text" data-error="Por favor ingrese una descripción del libro"></span>
            </div>
        </div>
        <div class="row">
            <div class="input-field col s12">
                <label for="genre" class="active">Género literario<span class="red-text">*</span></label>
                <select name="genre" id="genre"  >
                    <option value="" disabled> Seleccione género </option>
<%--                    <option value="Novela">Novela</option>
                    <option value="Poesía">Poesía</option>
                    <option value="Drama">Drama</option>
                    <option value="Ensayo">Ensayo</option>
                    <option value="Cuento">Cuento</option>
                    <option value="Fábula">Fábula</option>
                    <option value="Ciencia ficción">Ciencia ficción</option>
                    <option value="Fantasía">Fantasía</option>
                    <option value="Historia">Historia</option>
                    <option value="Biografía">Biografía</option>
                    <option value="Crónica">Crónica</option>
                    <option value="Epopeya">Epopeya</option>
                    <option value="Leyenda">Leyenda</option>
                    <option value="Mitología">Mitología</option>
                    <option value="Tragedia">Tragedia</option>
                    <option value="Comedia">Comedia</option>
                    <option value="Sátira">Sátira</option>
                    <option value="Diario">Diario</option>
                    <option value="Memorias">Memorias</option>--%>
                    <c:forEach items="${genres}" var="genre">
                        <option value="${genre}"><c:out value="${genre}"/></option>
                    </c:forEach>
                </select>
                <span class="helper-text" data-error="Por favor seleccione el género literario del libro"></span>
            </div>
        </div>
        <div class="row">
            <div class="input-field col s12">
                <label for="pageCount">Número de páginas<span class="red-text">*</span></label><br>
                <input id="pageCount" name="pageCount" class="validate" placeholder="Ejemplo: 300">
                <span class="helper-text" data-error="Por favor ingrese el número de páginas"></span>
            </div>
        </div>
        <div class="row">
                <div class="input-field col s12">
                    <label for="suggestedAge">Edad sugerida<span class="red-text">*</span></label><br>
                    <input type="number" id="suggestedAge" name="suggestedAge" class="validate" placeholder="Ejemplo: 18">
                    <span class="helper-text" data-error="Por favor ingrese una edad sugerida"></span>
                </div>
        </div>
        <div class="row">
            <div class="input-field col s12">
                <label for="price">Precio<span class="red-text">*</span></label><br>
                <input type="number" id="price" name="price" placeholder="Ejemplo: 20.99">
                <span class="helper-text" data-error="Por favor ingrese el precio del libro"></span>
            </div>
        </div>
        <div class="row">
            <div class="input-field col s12">
                <label for="image" class="active">Imagen de portada<span class="red-text">*</span></label><br>
                <input type="file" id="image" name="image" placeholder="Ejemplo: 123456" accept=".png, .jpeg">
                <span class="helper-text" data-error="Por favor ingrese el ID de la imagen"></span>
            </div>
        </div>
        <div class="row">
            <div class="input-field col s12">
                <label for="pdf">Previsualización del libro<span class="red-text">*</span></label><br>
                <input type="file" id="pdf" name="pdf" class="validate" placeholder="Breve previsualización del contenido" accept=".pdf">
                <span class="helper-text" data-error="Por favor ingrese una previsualización del libro"></span>
            </div>
        </div>
        <div class="row">
            <div class="input-field col s12">
                <button class="btn waves-effect waves-light" type="submit" name="action">
                    <i class="material-icons center">PUBLICAR</i>
                </button>
            </div>
        </div>
    </form>
</div>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        var elems = document.querySelectorAll('select');
        var instances = M.FormSelect.init(elems);
    });
</script>
</body>
</html>
