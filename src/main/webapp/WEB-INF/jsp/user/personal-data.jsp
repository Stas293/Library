<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>

<fmt:requestEncoding value="UTF-8" />
<fmt:setLocale value="${sessionScope.lang}" />
<fmt:setBundle basename="interface"/>

<tag:authorization>
    <jsp:attribute name="head">
        <title><c:out value="${sessionScope.user.fullName}" /></title>
    </jsp:attribute>
    <jsp:body>
        <div class="container main-content">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="/"><fmt:message key="home.pageTitle" /></a></li>
                <li class="breadcrumb-item active"><a href="/library/personal-data"><fmt:message key="user.dataPage" /></a></li>
            </ol>
            <h1><c:out value="${sessionScope.authority.fullName}" /></h1>
            <div>
                <div>
                    <div class="form-group row">
                        <div class="col-sm-2"><fmt:message key="user.login" /></div>
                        <div class="col-sm-6">${sessionScope.authority.login}</div>
                    </div>
                </div>
                <div>
                    <div class="form-group row">
                        <div class="col-sm-2"><fmt:message key="user.firstName" /></div>
                        <div class="col-sm-6">${sessionScope.authority.firstName}</div>
                    </div>
                    <div class="form-group row">
                        <div class="col-sm-2"><fmt:message key="user.lastName" /></div>
                        <div class="col-sm-6">${sessionScope.authority.lastName}</div>
                    </div>
                    <div class="form-group row">
                        <div class="col-sm-2"><fmt:message key="user.email" /></div>
                        <div class="col-sm-6">
                            <a href="mailto:${sessionScope.authority.email}">${sessionScope.authority.email}</a>
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="col-sm-2"><fmt:message key="user.phone" /></div>
                        <div class="col-sm-6">
                            <span>${sessionScope.authority.phone}</span>
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="col-sm-2"><fmt:message key="user.dateCreated" /></div>
                        <div class="col-sm-6">
                            <span>${sessionScope.authority.dateCreated}</span>
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="col-sm-2"><fmt:message key="user.dateUpdated" /></div>
                        <div class="col-sm-6">
                            <span>${sessionScope.authority.dateUpdated}</span>
                        </div>
                    </div>
                </div>
            </div>
            <a href="${editUserUrl}" class="btn btn-primary" title="${editUser}"><fmt:message key="user.edit" /></a>
        </div>
    </jsp:body>
</tag:authorization>