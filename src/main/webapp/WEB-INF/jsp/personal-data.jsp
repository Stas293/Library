<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>

<fmt:requestEncoding value="UTF-8" />
<fmt:setLocale value="${lang}" />
<fmt:setBundle basename="interface"/>

<c:url var="editUserUrl" value="library/personal-data/edit" />
<c:url var="editUserPasswordUrl" value="library/personal-data/editPassword" />

<tag:authorization>
    <jsp:attribute name="head">
        <title><c:out value="${user.fullName}" /></title>
    </jsp:attribute>
    <jsp:body>
        <div class="container main-content">
            <ol class="breadcrumb list-group-item-dark rounded">
                <li class="breadcrumb-item"><a href="/"><fmt:message key="home.pageTitle" /></a></li>
            </ol>
            <h1><c:out value="${authority.fullName}" /></h1>
            <c:if test="${param.edit_page == false}">
                <div class="info alert-danger"><fmt:message key="user.error.edit_page" /></div>
            </c:if>
            <c:if test="${param.edit == true}">
                <div class="info alert"><fmt:message key="user.edit.success" /></div>
            </c:if>
            <div>
                <div>
                    <div class="form-group row">
                        <div class="col-sm-2"><fmt:message key="user.login" /></div>
                        <div class="col-sm-6">${authority.login}</div>
                    </div>
                </div>
                <div>
                    <div class="form-group row">
                        <div class="col-sm-2"><fmt:message key="user.firstName" /></div>
                        <div class="col-sm-6">${authority.firstName}</div>
                    </div>
                    <div class="form-group row">
                        <div class="col-sm-2"><fmt:message key="user.lastName" /></div>
                        <div class="col-sm-6">${authority.lastName}</div>
                    </div>
                    <div class="form-group row">
                        <div class="col-sm-2"><fmt:message key="user.email" /></div>
                        <div class="col-sm-6">
                            <a href="mailto:${authority.email}">${authority.email}</a>
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="col-sm-2"><fmt:message key="user.phone" /></div>
                        <div class="col-sm-6">
                            <span>${authority.phone}</span>
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="col-sm-2"><fmt:message key="user.dateCreated" /></div>
                        <div class="col-sm-6">
                            <span>${authority.dateCreated}</span>
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="col-sm-2"><fmt:message key="user.dateUpdated" /></div>
                        <div class="col-sm-6">
                            <span>${authority.dateUpdated}</span>
                        </div>
                    </div>
                </div>
            </div>
            <a href="${editUserUrl}" class="btn btn-primary"><fmt:message key="userPage.editData" /></a>
            <a href="${editUserPasswordUrl}" class="btn btn-primary"><fmt:message key="userPage.editPassword" /></a>
        </div>
    </jsp:body>
</tag:authorization>