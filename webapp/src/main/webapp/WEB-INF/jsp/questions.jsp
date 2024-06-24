<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
    <title><spring:message code="topBar.questions"/></title>
    <script src="https://kit.fontawesome.com/0f001c5d7a.js" crossorigin="anonymous"></script>
    <link href="<c:url value="/css/questions.css"/>" rel="stylesheet"/>
    <link href="<c:url value="/css/paginationControls.css"/>" rel="stylesheet"/>
    <link href="<c:url value="/css/profile.css"/>" rel="stylesheet"/>

    <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/images/cybrary.png"/>"/>
</head>
<%@include file="components/topBar.jsp" %>
<body>
    <div class="container question-container">
        <h2 class="page-title"><spring:message code="topBar.questions"/></h2>
        <c:url var="bookInfoUrl" value="/questions"/>

        <c:if test="${isAuthor}">
            <div class="row table-top">
                <a href="${bookInfoUrl}/myQuestions">
                    <c:if test="${tab eq 'myQuestions'}">
                        <div class="col s6 table-title active">
                            <p class="text-active" style="width: 100%"><spring:message code="book.bookInfo.tab.myQuestions"/></p>
                        </div>
                    </c:if>
                    <c:if test="${tab ne 'myQuestions'}">
                        <div class="col s6 table-title">
                            <p class="text-not-active" style="width: 100%"><spring:message code="book.bookInfo.tab.myQuestions"/></p>
                        </div>
                    </c:if>
                </a>
                <a href="${bookInfoUrl}/questions">
                    <c:if test="${tab eq 'questions'}">
                        <div class="col s6 table-title active">
                            <p class="text-active" style="width: 100%"><spring:message code="book.bookInfo.tab.questions"/></p>
                        </div>
                    </c:if>
                    <c:if test="${tab ne 'questions'}">
                        <div class="col s6 table-title">
                            <p class="text-not-active" style="width: 100%"><spring:message code="book.bookInfo.tab.questions"/></p>
                        </div>
                    </c:if>
                </a>
            </div>
        </c:if>

        <c:if test="${(tab eq 'myQuestions' and empty myQuestions.page) or (tab eq 'questions' and empty questions.page)}">
            <div class="container question-container centerer">
                <h6><spring:message code="book.bookInfo.questions.noQuestions"/></h6>
            </div>
        </c:if>

        <c:if test="${tab eq 'myQuestions' and not empty myQuestions.page}">
            <ul class="collection">
                <c:forEach var="question" items="${myQuestions.page}">
                    <c:set var="question" value="${question}" scope="request"/>
                    <c:set var="isQuestioner" value="${tab eq 'myQuestions'}" scope="request"/>
                    <c:set var="answerForm" value="${answerForm}" scope="request"/>
                    <%@include file="components/questionCard.jsp"%>
                </c:forEach>
            </ul>
        </c:if>

        <c:if test="${tab eq 'questions' and not empty questions.page}">
            <c:url value="/questions/questions" var="questionsUrl"/>
            <form:form modelAttribute="filterQuestionsForm"
                       action="${questionsUrl}"
                       method="get"
                       id="questions">
                <div class="row">
                    <label path="showComplete" id="showComplete">
                        <input type="checkbox" path="showComplete" name="ShowComplete" onchange="this.form.submit()" ${showComplete ? 'checked' : ''}/>
                        <span><spring:message code="book.questions.viewComplete"/></span>
                    </label>
                </div>
                <input type="submit" hidden />
                <input name="page" id="page" style="display: none"/>
            </form:form>
            <ul class="collection">
                <c:forEach var="question" items="${questions.page}">
                    <c:set var="question" value="${question}" scope="request"/>
                    <c:set var="isQuestioner" value="${tab eq 'myQuestions'}" scope="request"/>
                    <c:set var="answerForm" value="${answerForm}" scope="request"/>
                    <%@include file="components/questionCard.jsp"%>
                </c:forEach>
            </ul>
        </c:if>

        <c:if test="${pageCount gt 1}">
            <script src="<c:url value="/js/paginationControls.js"/>"></script>
            <script>
                const paginationButtonsQuestions = new PaginationButtons(${pageCount}, Math.min(10,${pageCount}), ${pageNumber}, false);
                paginationButtonsQuestions.render();
                paginationButtonsQuestions.onChange(e => {
                    window.location.href = "<c:url value="?page="/>" + e.target.value;
                });
            </script>
        </c:if>
        <script>
            document.getElementById('showComplete').addEventListener('change', function() {
                document.getElementById('showComplete').submit();
            });
        </script>
    </div>
</body>
</html>
