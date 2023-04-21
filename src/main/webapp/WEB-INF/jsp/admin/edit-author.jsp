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
        <title><fmt:message key="editAuthor.pageTitle"/></title>
    </jsp:attribute>
    <jsp:body>
        <div class="container main-content">
            <ol class="breadcrumb list-group-item-dark rounded">
                <li class="breadcrumb-item"><a href="/"><fmt:message key="home.pageTitle"/></a></li>
                <li class="breadcrumb-item active">
                    <a href="/library/authors/admin/page">
                        <fmt:message key="authorList.pageTitle"/>
                    </a>
                </li>
            </ol>
            <h1><fmt:message key="editAuthor.pageTitle"/></h1>

            <form class="form-group" method="post" action="/library/authors/admin/${author.id}">
                <input type="hidden" name="id" value="${author.id}"/>
                <input type="hidden" name="_method" value="PUT"/>
                <div class="mb-3">
                    <label for="firstName">
                        <fmt:message key="editAuthor.firstName"/>
                    </label>
                    <c:set var="userInfo">${author.firstName}</c:set>
                    <c:if test="${not empty userInfo}">
                        <c:set var="authorFirstName">${author.firstName}</c:set>
                    </c:if>
                    <input name="firstName" id="firstName" class="form-control" value=${authorFirstName}>
                    <c:set var="firstNameErrors">${errors.firstName}</c:set>
                    <c:if test="${not empty firstNameErrors}">
                        <div class="alert alert-danger"><fmt:message key="${errors.firstName}"/></div>
                    </c:if>
                </div>
                <div class="mb-3">
                    <label for="middleName">
                        <fmt:message key="editAuthor.middleName"/>
                    </label>
                    <c:set var="userInfo">${author.middleName}</c:set>
                    <c:if test="${not empty userInfo}">
                        <c:set var="authorMiddleName">${author.middleName}</c:set>
                    </c:if>
                    <input name="middleName" id="middleName" class="form-control" value=${authorMiddleName}>
                    <c:set var="firstNameErrors">${errors.middleName}</c:set>
                    <c:if test="${not empty firstNameErrors}">
                        <div class="alert alert-danger"><fmt:message key="${errors.middleName}"/></div>
                    </c:if>
                </div>
                <div class="mb-3">
                    <label for="lastName">
                        <fmt:message key="editAuthor.lastName"/>
                    </label>
                    <c:set var="userInfo">${author.lastName}</c:set>
                    <c:if test="${not empty userInfo}">
                        <c:set var="authorLastName">${author.lastName}</c:set>
                    </c:if>
                    <input name="lastName" id="lastName" class="form-control" value=${authorLastName}>
                    <c:set var="firstNameErrors">${errors.lastName}</c:set>
                    <c:if test="${not empty firstNameErrors}">
                        <div class="alert alert-danger"><fmt:message key="${errors.lastName}"/></div>
                    </c:if>
                </div>
                <div class="form-group">
                    <input type="submit" class="btn btn-primary" value="<fmt:message key="editUser.label.submit" />"/>
                </div>
            </form>
        </div>
        <script type="text/javascript">
            $(function () {
                $('form').submit(function () {
                    $(':submit', this).attr('disabled', 'disabled');
                });
            })
        </script>
    </jsp:body>
</tag:authorization>