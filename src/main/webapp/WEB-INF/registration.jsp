<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>

<fmt:requestEncoding value="UTF-8"/>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="interface"/>

<tag:authorization>
    <jsp:attribute name="head">
        <title><fmt:message key="newUserRegistration.pageTitle"/></title>
        <script src="https://www.google.com/recaptcha/api.js"></script>
    </jsp:attribute>
    <jsp:body>
        <div class="container mt-5 main-content">
            <h1 class="text-center mb-4"><fmt:message key="newUserRegistration.pageTitle"/></h1>
            <form action="/library/register" method="post">
                <p><fmt:message key="newUserRegistration.message.allFieldsRequired"/></p>
                <div class="mb-3">
                    <label for="login">
                        <fmt:message key="newUserRegistration.label.login"/>
                    </label>
                    <c:set var="userInfo">${user_reg.login}</c:set>
                    <c:if test="${not empty userInfo}">
                        <c:set var="loginFieldValue">${user_reg.login}</c:set>
                    </c:if>
                    <input required name="login" type="text" class="form-control" id="login" value=${loginFieldValue}>
                    <c:set var="loginErrors">${errors.login}</c:set>
                    <c:if test="${not empty loginErrors}">
                        <div class="alert alert-danger"><fmt:message key="${errors.login}"/></div>
                    </c:if>
                </div>
                <div class="mb-3">
                    <label for="password" class="form-label">
                        <fmt:message key="newUserRegistration.label.password"/>
                    </label>
                    <input required name="password" type="password" class="form-control" id="password"/>
                </div>
                <div class="mb-3">
                    <label for="confirmPassword" class="form-label">
                        <fmt:message key="newUserRegistration.label.confirmPassword"/>
                    </label>
                    <input required name="confirmPassword" type="password" class="form-control" id="confirmPassword"/>
                    <c:set var="passwordErrors">${errors.password}</c:set>
                    <c:if test="${not empty passwordErrors}">
                        <div class="alert alert-danger"><fmt:message key="${errors.password}"/></div>
                    </c:if>
                </div>
                <div class="mb-3">
                    <label for="firstName" class="form-label">
                        <fmt:message key="newUserRegistration.label.firstName"/>
                    </label>
                    <c:set var="userInfo">${user_reg.firstName}</c:set>
                    <c:if test="${not empty userInfo}">
                        <c:set var="firstNameFieldValue">${user_reg.firstName}</c:set>
                    </c:if>
                    <input required name="firstName" type="text" class="form-control" id="firstName"
                           value=${firstNameFieldValue}>
                    <c:set var="firstNameErrors">${errors.firstName}</c:set>
                    <c:if test="${not empty firstNameErrors}">
                        <div class="alert alert-danger"><fmt:message key="${errors.firstName}"/></div>
                    </c:if>
                </div>
                <div class="mb-3">
                    <label for="lastName" class="form-label">
                        <fmt:message key="newUserRegistration.label.lastName"/>
                    </label>
                    <c:set var="userInfo">${user_reg.lastName}</c:set>
                    <c:if test="${not empty userInfo}">
                        <c:set var="lastNameFieldValue">${user_reg.lastName}</c:set>
                    </c:if>
                    <input required name="lastName" type="text" class="form-control" id="lastName"
                           value=${lastNameFieldValue}>
                    <c:set var="lastNameErrors">${errors.lastName}</c:set>
                    <c:if test="${not empty lastNameErrors}">
                        <div class="alert alert-danger"><fmt:message key="${errors.lastName}"/></div>
                    </c:if>
                </div>
                <div class="mb-3">
                    <label for="email" class="form-label">
                        <fmt:message key="newUserRegistration.label.email"/>
                    </label>
                    <c:set var="userInfo">${user_reg.email}</c:set>
                    <c:if test="${not empty userInfo}">
                        <c:set var="emailFieldValue">${user_reg.email}</c:set>
                    </c:if>
                    <input required name="email" type="email" class="form-control" id="email" value=${emailFieldValue}>
                    <c:set var="emailErrors">${errors.email}</c:set>
                    <c:if test="${not empty emailErrors}">
                        <div class="alert alert-danger"><fmt:message key="${errors.email}"/></div>
                    </c:if>
                </div>
                <div class="mb-3">
                    <label for="phone" class="form-label">
                        <fmt:message key="newUserRegistration.label.phone"/>
                    </label>
                    <c:set var="userInfo">${user_reg.phone}</c:set>
                    <c:if test="${not empty userInfo}">
                        <c:set var="phoneFieldValue">${user_reg.phone}</c:set>
                    </c:if>
                    <input required name="phone" type="text" class="form-control" id="phone" value=${phoneFieldValue}>
                    <c:set var="phoneErrors">${errors.phone}</c:set>
                    <c:if test="${not empty phoneErrors}">
                        <div class="alert alert-danger"><fmt:message key="${errors.phone}"/></div>
                    </c:if>
                </div>
                <div class="form-group" class="mb-3">
                    <label hidden="hidden" id="recaptchaLabel"><fmt:message key="newUserRegistration.input.recaptcha"/></label>
                    <div class="g-recaptcha" data-sitekey="6LeBIPUjAAAAABOzDNe4GDN4WdhheYH2Oax_bdJv"></div>
                    <c:set var="captchaErrors">${errors.captcha}</c:set>
                    <c:if test="${not empty captchaErrors}">
                        <div class="alert alert-danger"><fmt:message key="${errors.captcha}"/></div>
                    </c:if>
                </div>
                <button type="submit" class="btn btn-primary">
                    <fmt:message key="newUserRegistration.label.submit"/>
                </button>
            </form>
        </div>
        <script type="text/javascript">
            $(document).ready(function () {
                $('form').submit(function () {
                    if (grecaptcha.getResponse() === '') {
                        alert(document.getElementById('recaptchaLabel').innerText);
                        return false;
                    } else {
                        $(':submit', this).attr('disabled', 'disabled');
                    }
                });
            });
        </script>
    </jsp:body>
</tag:authorization>