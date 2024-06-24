<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons"/>
</head>
<body>
<div class="container question-container">
    <section id="myQuestions">
        <c:if test="${myQuestions ne null}">
            <c:if test="${empty myQuestions.page}">
                <h6><spring:message code="book.bookInfo.questions.noQuestions"/></h6>
            </c:if>
            <c:forEach var="question" items="${myQuestions.page}">
                <span class="question"><c:out value="${question.question}"/> </span>
                <span class="date"><c:out value="${question.getFormattedDate(pageContext.request.locale)}"/></span>
                <div class="answer">
                    <c:if test="${question.answer ne null}">
                        <i class="material-icons prefix">subdirectory_arrow_right</i>
                        <span><c:out value="${question.answer}"/></span>&nbsp;
                        <span class="date"><c:out value="${question.getFormattedAnswerDate(pageContext.request.locale)}"/></span>
                    </c:if>
                    <c:if test="${question.answer eq null}">
                        <i class="material-icons prefix">subdirectory_arrow_right</i>
                        <span><spring:message code="book.bookInfo.questions.noAnswer"/></span>
                    </c:if>
                </div>
                <br/>
            </c:forEach>
        </c:if>
    </section>
</div>
</body>

</html>
