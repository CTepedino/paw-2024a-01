<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
    <link rel="stylesheet" href="<c:url value="/css/questions.css"/>"/>
</head>
<body>

<div class="qa-list">

    <c:if test="${not isAuthor}">
        <h5><spring:message code="book.bookInfo.questions.otherQuestions"/></h5>
    </c:if>
    <c:if test="${questions.page.size()==0}">
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
                <c:url value="/book/${bookId}/${question.questionId}/answer" var="answerPostUrl"/>
                <%--@elvariable id="answerForm" type="AnswerForm"--%>
                <form:form
                        modelAttribute="answerForm"
                        action="${answerPostUrl}"
                        method="post"
                        class="answer-form"
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
    <c:if test="${questions.pageCount gt 1}">
        <script src="<c:url value="/js/paginationControls.js"/>"></script>
        <script>
            const paginationButtonsQuestions = new PaginationButtons(${questions.pageCount}, Math.min(10,${questions.pageCount}), ${questions.pageNumber}, false);
            paginationButtonsQuestions.render();
            paginationButtonsQuestions.onChange(e => {
                window.location.href = "<c:url value="?questionsPage="/>" + e.target.value + "#questions";
            });
        </script>
    </c:if>
</div>

</body>
</html>
