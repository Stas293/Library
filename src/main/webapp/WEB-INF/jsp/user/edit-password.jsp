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
        <title><fmt:message key="personal.edit.password"/></title>
    </jsp:attribute>
    <jsp:body>
        <div class="container main-content">
            <ol class="breadcrumb list-group-item-dark rounded">
                <li class="breadcrumb-item"><a href="/"><fmt:message key="home.pageTitle"/></a></li>
                <li class="breadcrumb-item active"><a href="/library/user/personal-data"><fmt:message key="personal.pageTitle"/></a></li>
            </ol>
            <h1><fmt:message key="personal.edit.password"/></h1>
            <form class="form-floating" method="post">
                <input type="hidden" name="_method" value="PUT"/>
                <p><fmt:message key="personal.edit.password.allFieldsRequired"/></p>
                <div class="mb-3">
                    <label for="oldPassword">
                        <fmt:message key="personal.edit.password.check"/>
                    </label>
                    <input required name="oldPassword" type="password" class="form-control" id="oldPassword"/>
                    <c:set var="oldPasswordError">${errors.oldPassword}</c:set>
                    <c:if test="${not empty oldPasswordError}">
                        <div class="alert alert-danger"><fmt:message key="${errors.oldPassword}"/></div>
                    </c:if>
                </div>
                <div class="mb-3">
                    <label for="password">
                        <fmt:message key="personal.edit.password.new"/>
                    </label>
                    <input required name="password" type="password" class="form-control" id="password"/>
                    <c:set var="passwordError">${errors.password}</c:set>
                    <c:if test="${not empty passwordError}">
                        <div class="alert alert-danger"><fmt:message key="${errors.password}"/></div>
                    </c:if>
                </div>
                <div class="mb-3">
                    <label for="confirmPassword">
                        <fmt:message key="personal.edit.password.confirmPassword"/>
                    </label>
                    <input required name="confirmPassword" type="password" class="form-control" id="confirmPassword"/>
                    <c:set var="confirmPasswordError">${errors.confirmPassword}</c:set>
                    <c:if test="${not empty confirmPasswordError}">
                        <div class="alert alert-danger"><fmt:message key="${errors.confirmPassword}"/></div>
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