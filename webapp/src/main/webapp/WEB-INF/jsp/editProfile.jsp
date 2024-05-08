<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Profile</title>
    <link href="${pageContext.request.contextPath}/css/userForm.css" rel="stylesheet"/>
</head>
<%@include file="components/materializeComponent.jsp"%>
<c:set var="hideSearchBar" value="${true}" scope="request"/>
<jsp:include page="components/topBar2.0.jsp" />

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
                Editar Perfil
            </h5>

            <div class="input-field">
                <form:label path="newFirstName">
                    Nombre
                </form:label>
                <form:input path="newFirstName" value="${user.firstName}"/>
            </div>
            <form:errors path="newFirstName" cssClass="red-text" element="p"/>

            <div class="input-field">
                <form:label path="newLastName">
                    Apellido
                </form:label>
                <form:input path="newLastName" value="${user.lastName}"/>
            </div>
            <form:errors path="newLastName" cssClass="red-text" element="p"/>


            <c:if test="${hasWriterRole}">
                <h6><spring:message code="book.addBook.cbuTitle"/></h6>
                <div class="input-field">
                    <form:label path="cbu"><spring:message code="book.addBook.cbu"/><span class="red-text">*</span></form:label><br>
                    <form:input type="text" path="cbu" inputmode="numeric" value="${user.cbu}"/>
                </div>
                <form:errors path="cbu" cssClass="red-text" element="p"/>
            </c:if>


            <div class="input-field">
                <form:label path="profilePicture" cssClass="active">Sube tu foto de perfil<span class="red-text">*</span> (.png, .jpeg)</form:label>
                <form:input type="file" path="profilePicture" accept="image/*"/>
            </div>
            <form:errors path="profilePicture" cssClass="red-text"  element="p"/>


            <div class="input-field center-align submit-btn">
                <button class="btn waves-effect waves-light white-text" id="submitBtn" type="submit" name="action">
                    Guardar Cambios
                </button>
            </div>
        </form:form>
    </div>
</div>
</body>
</html>

