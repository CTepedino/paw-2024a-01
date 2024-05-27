<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <link rel="stylesheet" href="<c:url value="/css/questions.css"/>"/>
</head>
<body>
        <section class="qa-section">
            <h5><spring:message code="book.bookInfo.questions.title"/></h5>
            <form id="question-form">
                <div class="row">
                    <div class="input-field col s9">
                        <i class="material-icons prefix">question_answer</i>
                        <textarea id="question" name="question" class="materialize-textarea" placeholder="<spring:message code="book.bookInfo.questions.placeholder"/>"></textarea>
                        <label for="question"><spring:message code="book.bookInfo.questions.label"/></label>
                    </div>
                    <div class="col s3">
                        <button class="btn waves-effect waves-light" type="submit">
                            <spring:message code="book.bookInfo.questions.send"/>
                            <i class="material-icons right">send</i>
                        </button>
                    </div>
                </div>
            </form>

            <div class="qa-list">
                <p class="question"><c:out value="¿Cuándo será la próxima actualización del libro?"/></p>
                <div class="answer">
                    <i class="material-icons prefix">subdirectory_arrow_right</i>
                    <span><c:out value="La próxima actualización está programada para el próximo mes."/></span>
                </div>
            </div>
        </section>
</body>
</html>
