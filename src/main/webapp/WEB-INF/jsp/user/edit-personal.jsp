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
        <title><fmt:message key="personal.edit.data"/></title>
    </jsp:attribute>
    <jsp:body>
        <div class="container main-content">
            <ol class="breadcrumb list-group-item-dark rounded">
                <li class="breadcrumb-item"><a href="/"><fmt:message key="home.pageTitle"/></a></li>
                <li class="breadcrumb-item active"><a href="/library/user/personal-data"><fmt:message
                        key="personal.pageTitle"/></a></li>
            </ol>
            <h1><fmt:message key="personal.edit.data"/></h1>
            <form class="form-floating" method="post">
                <input type="hidden" name="_method" value="PUT"/>
                <p><fmt:message key="personal.edit.data.allFieldsRequired"/></p>
                <div class="mb-3">
                    <label for="firstName">
                        <fmt:message key="personal.edit.data.firstName"/>
                    </label>
                    <c:set var="userInfo">${account.firstName}</c:set>
                    <c:if test="${not empty userInfo}">
                        <c:set var="firstNameFieldValue">${account.firstName}</c:set>
                    </c:if>
                    <input required name="firstName" type="text" class="form-control" id="firstName"
                           value=${firstNameFieldValue}>
                    <c:set var="firstNameErrors">${errors.firstName}</c:set>
                    <c:if test="${not empty firstNameErrors}">
                        <div class="alert alert-danger"><fmt:message key="${errors.firstName}"/></div>
                    </c:if>
                </div>
                <div class="mb-3">
                    <label for="lastName">
                        <fmt:message key="personal.edit.data.lastName"/>
                    </label>
                    <c:set var="userInfo">${account.lastName}</c:set>
                    <c:if test="${not empty userInfo}">
                        <c:set var="lastNameFieldValue">${account.lastName}</c:set>
                    </c:if>
                    <input required name="lastName" type="text" class="form-control" id="lastName"
                           value=${lastNameFieldValue}>
                    <c:set var="lastNameErrors">${errors.lastName}</c:set>
                    <c:if test="${not empty lastNameErrors}">
                        <div class="alert alert-danger"><fmt:message key="${errors.lastName}"/></div>
                    </c:if>
                </div>
                <div class="mb-3">
                    <label for="email">
                        <fmt:message key="personal.edit.data.email"/>
                    </label>
                    <c:set var="userInfo">${account.email}</c:set>
                    <c:if test="${not empty userInfo}">
                        <c:set var="emailFieldValue">${account.email}</c:set>
                    </c:if>
                    <input required name="email" type="email" class="form-control" id="email" value=${emailFieldValue}>
                    <c:set var="emailErrors">${errors.email}</c:set>
                    <c:if test="${not empty emailErrors}">
                        <div class="alert alert-danger"><fmt:message key="${errors.email}"/></div>
                    </c:if>
                </div>
                <div class="mb-3">
                    <label for="phone">
                        <fmt:message key="personal.edit.data.phone"/>
                    </label>
                    <c:set var="userInfo">${account.phone}</c:set>
                    <c:if test="${not empty userInfo}">
                        <c:set var="phoneFieldValue">${account.phone}</c:set>
                    </c:if>
                    <input required name="phone" type="text" class="form-control" id="phone" value=${phoneFieldValue}>
                    <c:set var="phoneErrors">${errors.phone}</c:set>
                    <c:if test="${not empty phoneErrors}">
                        <div class="alert alert-danger"><fmt:message key="${errors.phone}"/></div>
                    </c:if>
                </div>
                <button type="submit" class="btn btn-primary">
                    <fmt:message key="personal.edit.data.submit"/>
                </button>
            </form>
        </div>
        <script type="text/javascript">
            $(document).ready(function () {
                $('form').submit(function () {
                    $(':submit', this).attr('disabled', 'disabled');
                });
            });
        </script>
    </jsp:body>
</tag:authorization>