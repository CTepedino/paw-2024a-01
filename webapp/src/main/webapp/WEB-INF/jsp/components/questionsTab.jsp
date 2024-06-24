<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
    <link href="<c:url value="/css/paginationControls.css"/>" rel="stylesheet"/>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons"/>
</head>
<body>
<div class="container question-container">
    <div class="qa-list">
        <c:if test="${empty questions.page}">
            <h6><spring:message code="book.bookInfo.questions.noQuestions"/></h6>
        </c:if>

        <c:forEach var="question" items="${questions.page}">
            <c:if test="${question.answer ne null}">
                <span class="question"><c:out value="${question.question}"/> </span>
                <span class="date"><c:out value="${question.getFormattedDate(pageContext.request.locale)}"/></span>
                <div class="answer">
                    <i class="material-icons prefix">subdirectory_arrow_right</i>
                    <span><c:out value="${question.answer}"/></span>&nbsp
                    <span class="date"><c:out value="${question.getFormattedAnswerDate(pageContext.request.locale)}"/></span>
                </div>
                <br/>
            </c:if>
            <c:if test="${question.answer eq null and isAuthor}">
                <span class="question"><c:out value="${question.question}"/> </span>
                <span class="date"><c:out value="${question.getFormattedDate(pageContext.request.locale)}"/></span>
                <div class="answer">
                    <c:url value="/book/${bookId}/questions/${question.questionId}/answer" var="answerPostUrl"/>
                    <%--@elvariable id="answerForm" type="AnswerForm"--%>
                    <form:form
                            modelAttribute="answerForm"
                            action="${answerPostUrl}"
                            method="post"
                            class="answer-form"
                            id="question-${question.questionId}"
                    >
                        <div class="row">
                            <div class="input-field col s9 answer-input">
                                <i class="material-icons prefix">subdirectory_arrow_right</i>
                                <form:textarea class="materialize-textarea" type="text" path="answer"/>
                                <form:errors path="answer" cssClass="red-text" element="p"/>
                            </div>
                            <div class="col s3">
                                <button class="btn waves-effect waves-light" type="submit" name="action">
                                    <spring:message code="book.bookInfo.answer.send"/>
                                    <i class="material-icons right">send</i>
                                </button>
                            </div>
                        </div>
                    </form:form>
                </div>
            </c:if>
        </c:forEach>
    </div>
</div>

</body>
</html>
