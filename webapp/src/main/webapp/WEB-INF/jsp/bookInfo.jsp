<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:eval expression="@environment.getProperty('base.url')" var="baseUrl"/>
<html>
<head>
    <title>Title</title>
    <link href="${pageContext.request.contextPath}/css/bookInfo.css" rel="stylesheet"/>
</head>
<body>
    <%@ include file="components/topBar.jsp" %>

    <div class="book-container z-depth-2" style="margin: 30px;padding: 20px;">
        <div class="row">
            <div class="col s5">
                <img class="book_cover" src="<c:url  value="${baseUrl}/image/${book.imageId}"/>" alt="Book cover">
            </div>
            <div class="col s7">
                <h2>${book.title}</h2>
                <div class="row">
                    <div class="col s8">
                        <h5>${book.writerName} ${book.writerLastName}</h5>
                    </div>
                    <div class="col s4">
                        <a class="waves-effect waves-light btn"  href="${pageContext.request.contextPath}/buy?writerEmail=${book.writerEmail}&bookTitle=${book.title}">Contact Writer</a>
                    </div>
                </div>
                <h5>$${book.price}</h5>
                <table>
                    <tbody>
                    <tr>
                        <td>Recommended age</td>
                        <td>${book.suggestedAge}</td>
                    </tr>
                    <tr>
                        <td>Genre</td>
                        <td>${book.genre}</td>
                    </tr>
                    <tr>
                        <td>Page count</td>
                        <td>${book.pageCount}</td>
                    </tr>
                    <tr>
                        <td>Publish date</td>
                        <td>${book.publishDate}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="col s12">
                <p>${book.description}</p>
            </div>
            <div class="col s12">
                <h6>Preview: </h6>
                <object
                        type="application/pdf"
                        data="<c:url value="${baseUrl}/pdf/${book.previewPdfId}" />"
                        width="100%"
                        height="700"
                >
                </object>
            </div>
        </div>
    </div>

</body>
</html>
