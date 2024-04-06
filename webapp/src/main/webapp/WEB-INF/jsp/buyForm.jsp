<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
    <title>Contact Information</title>
</head>
<body>
    <%@ include file="components/topBar.jsp" %>
    <c:url value="/sendBuyInfo" var="registerUrl"/>
        <div class="row">
            <div class="col s4 push-s4">
                <div class="card">
                    <div class="card-content">
                        <span class="card-title">Enter Your Contact Information</span>
                        <div class="row">
                            <span class="card-subtitle col s12">The information will be sent to the seller.</span>
                        </div>
                        <div class="row">
                            <form action="${registerUrl}" method="post" class="col s12">
                                <input name="writerId" value="${writerId}" hidden="hidden">
                                <div class="row">
                                    <div class="input-field col s6">
                                        <input name="name" id="name" type="text" class="validate" required maxlength="15">
                                        <label for="name">First Name</label>
                                    </div>
                                    <div class="input-field col s6">
                                        <input name="lastName" id="lastName" type="text" class="validate" required maxlength="15">
                                        <label for="lastName">Last Name</label>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="input-field col s12">
                                        <input name="email" id="email" type="email" class="validate" required maxlength="50">
                                        <label for="email">Email</label>
                                    </div>
                                </div>
                                <div>
                                    <button class="btn waves-effect waves-light" type="submit" name="action">Submit
                                        <i class="material-icons right">send</i>
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
