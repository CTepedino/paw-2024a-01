<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><spring:message code="book.addBook.publish"/></title>
    <link href="<c:url value="/css/addBook.css"/>" rel="stylesheet"/>

    <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/images/cybrary.png"/>"/>
</head>

<c:set value="${true}" scope="request" var="hideSearchBar"/>
<%@include file="components/topBar.jsp" %>

<body>
<div class="container publish-container">
    <div class="form">
        <c:url value="/addBook" var="postUrl"/>
        <form:form
                modelAttribute="newBookForm"
                action="${postUrl}"
                method="post"
                enctype="multipart/form-data"
                cssClass="z-depth-2"
        >
            <h5 class="publish-title"><spring:message code="book.addBook.pageTitle"/></h5>
            <c:if test="${!isWriter}">
                <br/>
                <h6><spring:message code="book.addBook.cbuTitle"/></h6>
                <div class="input-field">
                    <form:label path="cbu"><spring:message code="book.addBook.cbu"/><span class="red-text">*</span></form:label>
                    <form:input type="text" path="cbu" inputmode="numeric" maxlength="22"/>
                </div>
                <form:errors path="cbu" cssClass="red-text" element="p"/>
                <br/>
                <div class="divider"></div>
                <br/>
            </c:if>

            <h6><spring:message code="book.addBook.bookTitle"/></h6>

            <div class="input-field">
                <form:label path="title"><spring:message code="book.addBook.title"/><span class="red-text">*</span></form:label>
                <form:input type="text" path="title" maxlength="50"/>
            </div>
            <form:errors path="title" cssClass="red-text" element="p"/>
            <br>

            <div class="input-field">
                <form:label path="description"><spring:message code="book.addBook.description"/><span class="red-text">*</span></form:label>
                <form:textarea path="description" maxlength="1000" class="materialize-textarea"/>
            </div>
            <form:errors path="description" cssClass="red-text" element="p"/>
            <br>
            <div class="input-field">
                <form:label path="genre" cssClass="active"><spring:message code="book.addBook.genre"/><span class="red-text">*</span></form:label>
                <form:select path="genre">
                    <form:option value="" disabled="true"> <spring:message code="book.addBook.genreTitle"/> </form:option>
                    <c:forEach items="${genres}" var="genre">
                        <form:option value="${genre}"><spring:message code="book.genre.${genre}"/></form:option>
                    </c:forEach>
                </form:select>
            </div>
            <form:errors cssClass="red-text"  path="genre" element="p"/>
            <br>
            <div class="input-field">
                <form:label path="pageCount"><spring:message code="book.addBook.pageCount"/><span class="red-text">*</span></form:label>
                <form:input type="number" path="pageCount" min="0" max="1000000"/>
            </div>
            <form:errors path="pageCount" cssClass="red-text"  element="p"/>
            <br>
            <div class="input-field">
                <form:label path="suggestedAge"><spring:message code="book.addBook.recommendedAge"/><span class="red-text">*</span></form:label>
                <form:input type="number" path="suggestedAge" min="0" max="100"/>
            </div>
            <form:errors path="suggestedAge" cssClass="red-text"  element="p"/>
            <br>
            <div class="input-field">
                <form:label path="price"><spring:message code="book.addBook.price"/><span class="red-text">*</span></form:label>
                <form:input type="number" path="price" min="0" max="100000000"/>
            </div>
            <form:errors path="price" cssClass="red-text"  element="p"/>
            <br>

            <div>
                <form:label path="publicationDate"><spring:message code="book.addBook.date"/><span class="red-text">*</span></form:label>
                <form:input type="text" id="date" path="publicationDate" cssClass="datepicker"/>
            </div>
            <form:errors path="publicationDate" cssClass="red-text"  element="p"/>
            <br>

            <div class="file-field input-field">
                <div class="btn">
                    <span><spring:message code="book.addBook.cover"/></span>
                    <input type="file" accept="image/*" name="cover" id="cover">
                </div>
                <div class="file-path-wrapper">
                    <input class="file-path validate" type="text" \>
                </div>
            </div>
            <form:errors path="cover" cssClass="red-text"  element="p"/>


            <div class="file-field input-field">
                <div class="btn">
                    <span><spring:message code="book.addBook.preview"/></span>
                    <input type="file" accept="application/pdf" name="preview" id="preview">
                </div>
                <div class="file-path-wrapper">
                    <input class="file-path validate" type="text" \>
                </div>
            </div>
            <form:errors path="preview" cssClass="red-text"  element="p"/>

            <div class="file-field input-field">
                <div class="btn">
                    <span><spring:message code="book.addBook.bookFile"/></span>
                    <input type="file" accept="application/pdf" name="bookFile" id="bookFile">
                </div>
                <div class="file-path-wrapper">
                    <input class="file-path validate" type="text" \>
                </div>
            </div>
            <form:errors path="bookFile" cssClass="red-text"  element="p"/>

            <div class="input-field center">
                <button class="btn waves-effect waves-light" type="submit" name="action">
                    <strong><spring:message code="book.addBook.publish"/></strong>
                </button>
            </div>
        </form:form>

    </div>
</div>
<script>

    document.addEventListener('DOMContentLoaded', function () {
        var elems = document.querySelectorAll('select');
        var instances = M.FormSelect.init(elems);
    });

    document.addEventListener('DOMContentLoaded', function() {
        var elems = document.querySelectorAll('.datepicker');

        var options = {
            'maxDate': new Date(),
            'format': 'dd/mm/yyyy',
            'autoClose': true,
            'defaultDate': new Date(),
            'setDefaultDate': true,
            'i18n': {
                'done': '<spring:message code="datePicker.done"/>',
                'cancel': '<spring:message code="datePicker.cancel"/>',
                'clear': '<spring:message code="datePicker.clear"/>',
                'months': [
                    '<spring:message code="datePicker.months.jan"/>',
                    '<spring:message code="datePicker.months.feb"/>',
                    '<spring:message code="datePicker.months.mar"/>',
                    '<spring:message code="datePicker.months.apr"/>',
                    '<spring:message code="datePicker.months.may"/>',
                    '<spring:message code="datePicker.months.jun"/>',
                    '<spring:message code="datePicker.months.jul"/>',
                    '<spring:message code="datePicker.months.aug"/>',
                    '<spring:message code="datePicker.months.sep"/>',
                    '<spring:message code="datePicker.months.oct"/>',
                    '<spring:message code="datePicker.months.nov"/>',
                    '<spring:message code="datePicker.months.dec"/>'
                ],
                'monthsShort': [
                    '<spring:message code="datePicker.monthsShort.jan"/>',
                    '<spring:message code="datePicker.monthsShort.feb"/>',
                    '<spring:message code="datePicker.monthsShort.mar"/>',
                    '<spring:message code="datePicker.monthsShort.apr"/>',
                    '<spring:message code="datePicker.monthsShort.may"/>',
                    '<spring:message code="datePicker.monthsShort.jun"/>',
                    '<spring:message code="datePicker.monthsShort.jul"/>',
                    '<spring:message code="datePicker.monthsShort.aug"/>',
                    '<spring:message code="datePicker.monthsShort.sep"/>',
                    '<spring:message code="datePicker.monthsShort.oct"/>',
                    '<spring:message code="datePicker.monthsShort.nov"/>',
                    '<spring:message code="datePicker.monthsShort.dec"/>'
                ],
                'weekdaysShort': [
                    '<spring:message code="datePicker.weekDaysShort.sun"/>',
                    '<spring:message code="datePicker.weekDaysShort.mon"/>',
                    '<spring:message code="datePicker.weekDaysShort.tue"/>',
                    '<spring:message code="datePicker.weekDaysShort.wed"/>',
                    '<spring:message code="datePicker.weekDaysShort.thu"/>',
                    '<spring:message code="datePicker.weekDaysShort.fri"/>',
                    '<spring:message code="datePicker.weekDaysShort.sat"/>'
                ],
                'weekdaysAbbrev': [
                    '<spring:message code="datePicker.weekDaysAbbrev.sun"/>',
                    '<spring:message code="datePicker.weekDaysAbbrev.mon"/>',
                    '<spring:message code="datePicker.weekDaysAbbrev.tue"/>',
                    '<spring:message code="datePicker.weekDaysAbbrev.wed"/>',
                    '<spring:message code="datePicker.weekDaysAbbrev.thu"/>',
                    '<spring:message code="datePicker.weekDaysAbbrev.fri"/>',
                    '<spring:message code="datePicker.weekDaysAbbrev.sat"/>'
                ]
            },

        }

        var instances = M.Datepicker.init(elems, options);
    });



</script>
</body>
</html>
