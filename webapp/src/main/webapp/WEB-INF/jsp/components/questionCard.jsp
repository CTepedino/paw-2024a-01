<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:eval expression="@environment.getProperty('base.url')" var="baseUrl"/>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
    <link rel="stylesheet" href="<c:url value="/css/questions.css"/>"/>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons"/>

</head>
<body>
<li class="collection-item">
    <div class="row questioned-book">
        <div class="col s2">
            <a class="card-image waves-effect waves-block waves-light" href="${pageContext.request.contextPath}/book/${order.book.bookId}">
                <img
                        class="book_cover"
                        src="<c:url value="${baseUrl}/cover/${question.book.bookId}"/>"
                        alt="<spring:message code="bookInfoCard.cover"/>"
                />
            </a>
        </div>
        <div class="col s3 question-info">
            <a class="book-title" href="${pageContext.request.contextPath}/book/${question.book.bookId}"><c:out value="${question.book.title}"/></a>
            <c:if test="${!isQuestioner}">
                <p><spring:message code="book.questions.questioner"/> <a href="<c:url value="/profile/${question.questioner.userId}"/>"><c:out value="${question.questioner.firstName}"/> <c:out value="${question.questioner.lastName}"/></a></p>
            </c:if>
            <c:if test="${isQuestioner}">
                <p><a href="<c:url value="/profile/${question.book.writer.userId}"/>"><c:out value="${question.book.writer.firstName}"/> <c:out value="${question.book.writer.lastName}"/></a></p>
            </c:if>
        </div>
        <div class="col s7 question-info">
            <span class="question"><c:out value="${question.question}"/> </span>
            <span class="date"><c:out value="${question.getFormattedDate(pageContext.request.locale)}"/></span>
            <div class="answer">
                <c:if test="${question.answer ne null}">
                    <i class="material-icons prefix">subdirectory_arrow_right</i>
                    <span><c:out value="${question.answer}"/></span>&nbsp;
                    <span class="date"><c:out value="${question.getFormattedAnswerDate(pageContext.request.locale)}"/></span>
                </c:if>
                <c:if test="${question.answer eq null and isQuestioner}">
                    <i class="material-icons prefix">subdirectory_arrow_right</i>
                    <span><spring:message code="book.bookInfo.questions.noAnswer"/></span>
                </c:if>
                <c:if test="${question.answer eq null and !isQuestioner}">
                        <c:url value="/questions/questions/${question.questionId}/answer" var="answerPostUrl"/>
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
                </c:if>
            </div>
        </div>
    </div>
</li>
</body>
</html>
