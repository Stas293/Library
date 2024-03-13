<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>

<fmt:requestEncoding value="UTF-8"/>
<fmt:setLocale value="${lang}"/>
<fmt:setBundle basename="interface"/>

<c:url var="editUserUrl" value="personal-data/edit"/>
<c:url var="editUserPasswordUrl" value="personal-data/password"/>

<tag:authorization>
    <jsp:attribute name="head">
        <title><c:out value="${user.fullName}"/></title>
    </jsp:attribute>
    <jsp:body>
        <div class="container main-content">
            <nav aria-label="breadcrumb">
                <ol class="breadcrumb bg-dark rounded">
                    <li class="breadcrumb-item"><a href="/" class="text-light"><fmt:message key="home.pageTitle"/></a>
                    </li>
                </ol>
            </nav>
            <h1><c:out value="${authority.fullName}"/></h1>
            <c:if test="${param.edit_page == false}">
                <div class="alert alert-danger"><fmt:message key="user.error.edit_page"/></div>
            </c:if>
            <c:if test="${param.edit == true}">
                <div class="alert alert-success"><fmt:message key="user.edit.success"/></div>
            </c:if>
            <div class="row">
                <div class="col-md-6">
                    <div class="mb-3 row">
                        <label for="login" class="col-sm-2 col-form-label"><fmt:message key="user.login"/></label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="login" value="${authority.login}" disabled>
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <label for="firstName" class="col-sm-2 col-form-label"><fmt:message
                                key="user.firstName"/></label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="firstName" value="${authority.firstName}"
                                   disabled>
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <label for="lastName" class="col-sm-2 col-form-label"><fmt:message key="user.lastName"/></label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="lastName" value="${authority.lastName}"
                                   disabled>
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="mb-3 row">
                        <label for="email" class="col-sm-2 col-form-label"><fmt:message key="user.email"/></label>
                        <div class="col-sm-10">
                            <div class="input-group">
                                <input type="text" class="form-control" id="email" value="${authority.email}" disabled>
                                <a href="mailto:${authority.email}" class="btn btn-primary">Email</a>
                            </div>
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <label for="phone" class="col-sm-2 col-form-label"><fmt:message key="user.phone"/></label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="phone" value="${authority.phone}" disabled>
                        </div>
                    </div>
                </div>
            </div>
            <div class="d-flex justify-content-between">
                <a href="${editUserUrl}" class="btn btn-primary"><fmt:message key="userPage.editData"/></a>
                <a href="${editUserPasswordUrl}" class="btn btn-primary"><fmt:message key="userPage.editPassword"/></a>
            </div>
        </div>

    </jsp:body>
</tag:authorization>