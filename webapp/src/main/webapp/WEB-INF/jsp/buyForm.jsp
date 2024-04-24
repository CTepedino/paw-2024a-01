<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title>Contact Information</title>
</head>
<jsp:include page="components/topBar.jsp">
    <jsp:param name="hasWriterRole" value="${hasWriterRole}" />
</jsp:include>
<body>
    <c:url value="/sendBuyInfo" var="registerUrl"/>
    <div class="row">
        <div class="col s4 push-s4">
            <div class="card">
                <div class="card-content">
                    <span class="card-title"><spring:message code="buy.buyForm.title"/></span>
                    <div class="row">
                        <span class="card-subtitle col s12"><spring:message code="buy.buyForm.subtitle"/></span>
                    </div>
                    <div class="row">
                        <form action="${registerUrl}" method="post" class="col s12">
                            <input name="writerEmail" value="<c:out value="${writerEmail}"/>" hidden="hidden">
                            <input name="bookTitle" value="<c:out value="${bookTitle}"/>" hidden="hidden">
                            <div class="row">
                                <div class="input-field col s6">
                                    <input name="name" id="name" type="text" class="validate" required maxlength="15">
                                    <label for="name"><spring:message code="buy.buyForm.name"/></label>
                                </div>
                                <div class="input-field col s6">
                                    <input name="lastName" id="lastName" type="text" class="validate" required maxlength="15">
                                    <label for="lastName"><spring:message code="buy.buyForm.lastName"/></label>
                                </div>
                            </div>
                            <div class="row">
                                <div class="input-field col s12">
                                    <input name="email" id="email" type="email" class="validate" required maxlength="50">
                                    <label for="email"><spring:message code="buy.buyForm.email"/></label>
                                </div>
                            </div>
                            <div>
                                <button class="btn waves-effect waves-light" type="submit" name="action">
                                    <spring:message code="buy.buyForm.submit"/>
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
