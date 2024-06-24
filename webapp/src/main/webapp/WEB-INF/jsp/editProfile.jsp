<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <meta charset="UTF-8">
    <title><spring:message code="user.profile.edit.title"/></title>
    <link href="<c:url value="/css/userForm.css"/>" rel="stylesheet"/>

    <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/images/cybrary.png"/>"/>
</head>

<c:set value="${true}" scope="request" var="hideSearchBar"/>
<%@include file="components/topBar.jsp" %>

<body>

<div class="small-container">
    <div class="form">
        <c:url value="/editProfile" var="postUrl"/>
        <form:form
                modelAttribute="editProfileForm"
                action="${postUrl}"
                method="post"
                enctype="multipart/form-data"
                cssClass="z-depth-2"
        >
            <h5 class="center-align">
                <spring:message code="user.profile.edit.title"/>
            </h5>

            <div class="input-field">
                <form:label path="newFirstName"><spring:message code="user.profile.edit.firstName"/><span class="red-text">*</span></form:label>
                <form:input path="newFirstName" maxlength="50"/>
            </div>
            <form:errors path="newFirstName" cssClass="red-text" element="p"/>

            <div class="input-field">
                <form:label path="newLastName"><spring:message code="user.profile.edit.lastName"/><span class="red-text">*</span></form:label>
                <form:input path="newLastName" maxlength="50"/>
            </div>
            <form:errors path="newLastName" cssClass="red-text" element="p"/>


            <c:if test="${isWriter}">
                <div class="input-field">
                    <form:label path="cbu"><spring:message code="user.profile.edit.cbu"/><span class="red-text">*</span></form:label>
                    <form:input type="text" path="cbu" inputmode="numeric" maxlength="22"/>
                </div>
                <form:errors path="cbu" cssClass="red-text" element="p"/>

            </c:if>

            <div class="input-field">
                <form:label path="description"><spring:message code="book.addBook.description"/></form:label>
                <form:textarea path="description" maxlength="500" class="materialize-textarea"/>
            </div>
            <form:errors path="description" cssClass="red-text" element="p"/>


            <div class="file-field input-field">
                <div class="btn">
                    <span><spring:message code="user.profile.edit.pfp"/></span>
                    <input type="file" accept="image/*" name="picture" id="picture">
                </div>
                <div class="file-path-wrapper">
                    <input class="file-path validate" type="text" \>
                </div>
            </div>
            <form:errors path="picture" cssClass="red-text"  element="p"/>


            <div class="input-field center-align submit-btn">
                <button class="btn waves-effect waves-light white-text" id="submitBtn" type="submit" name="action">
                    <strong><spring:message code="user.profile.edit.save"/></strong>
                </button>
            </div>

        </form:form>
    </div>
</div>
</body>
</html>

