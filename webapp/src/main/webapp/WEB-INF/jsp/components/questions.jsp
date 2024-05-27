<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <link rel="stylesheet" href="<c:url value="/css/questions.css"/>"/>
</head>
<body>

            <h5><spring:message code="book.bookInfo.questions.title"/></h5>
            <c:if test="${not isAuthor}">
                <form id="question-form">
                    <div class="row">
                        <div class="input-field col s9">
                            <i class="material-icons prefix">question_answer</i>
                            <textarea id="question" name="question" class="materialize-textarea" placeholder="<spring:message code="book.bookInfo.questions.placeholder"/>"></textarea>
                            <label for="question"><spring:message code="book.bookInfo.questions.label"/></label>
                        </div>
                        <div class="col s3 send-button">
                            <button class="btn waves-effect waves-light" type="submit">
                                <spring:message code="book.bookInfo.questions.send"/>
                                <i class="material-icons right">send</i>
                            </button>
                        </div>
                    </div>
                </form>
            </c:if>
            <div class="qa-list">
                <c:forEach var="question" items="${questions}">
                    <c:if test="${question.answer ne null}">
                        <span class="question"><c:out value="${question.question}"/> </span>
                        <span class="date"><c:out value="${question.date}"/></span>
                        <div class="answer">
                            <i class="material-icons prefix">subdirectory_arrow_right</i>
                            <span><c:out value="${question.answer}"/></span>
                            <span class="date"><c:out value="${question.date}"/></span>
                        </div>
                        <br/>
                    </c:if>
                    <c:if test="${question.answer eq null and isAuthor}">
                        <span class="question"><c:out value="${question.question}"/> </span>
                        <span class="date"><c:out value="${question.date}"/></span>
                        <div class="answer">
                            <form class="answer-form" id="answer-form">
                                <div class="row">
                                    <div class="input-field col s9 answer-input">
                                        <i class="material-icons prefix">subdirectory_arrow_right</i>
                                        <textarea id="answer" name="answer" class="materialize-textarea" placeholder="<spring:message code="book.bookInfo.answer.label"/>"></textarea>
                                    </div>
                                    <div class="col s3">
                                        <button class="btn waves-effect waves-light" type="submit">
                                            <spring:message code="book.bookInfo.answer.send"/>
                                            <i class="material-icons right">send</i>
                                        </button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </c:if>
                </c:forEach>
            </div>

</body>
</html>
