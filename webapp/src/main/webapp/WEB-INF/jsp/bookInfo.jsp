<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:eval expression="@environment.getProperty('base.url')" var="baseUrl"/>

<!DOCTYPE html>
<html>
<head>
    <title><c:out value="${book.title}"/></title>
    <link href="${pageContext.request.contextPath}/css/bookInfo.css" rel="stylesheet"/>
</head>
<jsp:include page="components/topBar.jsp">
    <jsp:param name="hasWriterRole" value="${hasWriterRole}" />
</jsp:include>
<body>
    <div class="book-container z-depth-2" style="margin: 30px;padding: 20px;">
        <div class="row">
            <div class="col s5">
                <img
                        class="book_cover"
                        src="<c:url value="${baseUrl}/image/${book.coverId}"/>"
                        alt="<spring:message code="bookInfoCard.cover"/>"
                />
            </div>
            <div class="col s7">
                <h2>
                    <c:out value="${book.title}"/>
                </h2>
                <div class="row">
                    <div class="col s8">
                        <h5>
                            <spring:message var="author" code="book.bookInfo.author" arguments="${book.writer.firstName},${book.writer.lastName}"/>
                            <c:out value="${author}"/>
                        </h5>
                    </div>
                    <div class="col s4">
                       <c:if test="${user!=null && book.writer.email != user.email}">
                            <c:url var="buyUrl" value="/sendBuyInfo">
                                <c:param name="bookId" value="${book.bookId}" />
                            </c:url>
                            <form action="${buyUrl}" method="post">
                                <button type="submit" class="waves-effect waves-light btn">
                                    <spring:message code="book.bookInfo.buyBook"/>
                                </button>
                            </form>
                       </c:if>
                    </div>
                </div>
                <h5>$<c:out value="${book.price}"/></h5>
                <table>
                    <tbody>
                    <tr>
                        <td><spring:message code="book.bookInfo.recommendedAge"/></td>
                        <td><c:out value="${book.suggestedAge}"/></td>
                    </tr>
                    <tr>
                        <td><spring:message code="book.bookInfo.genre"/></td>
                        <td><spring:message code="book.genre.${book.genre}"/></td>
                    </tr>
                    <tr>
                        <td><spring:message code="book.bookInfo.pageCount"/></td>
                        <td><c:out value="${book.pageCount}"/></td>
                    </tr>
                    <tr>
                        <td><spring:message code="book.bookInfo.publishDate"/></td>
                        <td><c:out value="${book.publishDate}"/></td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="col s12">
                <p><c:out value="${book.description}"/></p>
            </div>
            <div class="col s12">
                <h6><spring:message code="book.bookInfo.preview"/></h6>
                <object
                        type="application/pdf"
                        data="<c:url value="${baseUrl}/pdf/${book.previewId}" />"
                        width="100%"
                        height="700"
                >
                </object>
            </div>
        </div>
    </div>

</body>
</html>
