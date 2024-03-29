<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>


<fmt:requestEncoding value="UTF-8"/>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="interface"/>

<c:url var="deleteUserUrl" value="/library/admin/manage"/>
<c:url var="disableUserUrl" value="/library/admin/manage"/>
<c:url var="enableUserUrl" value="/library/admin/manage"/>

<tag:authorization>
    <jsp:attribute name="head">
        <title><c:out value="${account.fullName}"/></title>
    </jsp:attribute>
    <jsp:body>
        <div class="container main-content">
            <ol class="breadcrumb list-group-item-dark rounded">
                <li class="breadcrumb-item"><a href="/"><fmt:message key="home.pageTitle"/></a></li>
                <li class="breadcrumb-item active"><a href="/library/admin/page"><fmt:message
                        key="usersList.pageTitle"/></a></li>
            </ol>
            <c:if test="${param.success == true}">
                <div class="info alert"><fmt:message key="editUser.user.save"/></div>
            </c:if>

            <h1><c:out value="${account.fullName}"/></h1>
            <form action="${deleteUserUrl}/${account.id}" method="post">
                <input type="hidden" name="_method" value="DELETE"/>
                <input type="submit" class="btn btn-danger" value=
                    <fmt:message key="user.delete"/>>
            </form>
            <c:if test="${account.enabled == true}">
                <form action="${disableUserUrl}/${account.id}/disable" method="post">
                    <input type="hidden" name="_method" value="PATCH"/>
                    <input type="submit" class="btn btn-warning" value=
                        <fmt:message key="user.disable"/>>
                </form>
            </c:if>
            <c:if test="${account.enabled == false}">
                <form action="${enableUserUrl}/${account.id}/enable" method="post">
                    <input type="hidden" name="_method" value="PATCH"/>
                    <input type="submit" class="btn btn-success" value=
                        <fmt:message key="user.enable"/>>
                </form>
            </c:if>
            <div>
                <div>
                    <div class="form-group row">
                        <div class="col-sm-2"><fmt:message key="user.login"/></div>
                        <div class="col-sm-6">${account.login}</div>
                    </div>
                </div>
                <div>
                    <div class="form-group row">
                        <div class="col-sm-2"><fmt:message key="user.firstName"/></div>
                        <div class="col-sm-6">${account.firstName}</div>
                    </div>
                    <div class="form-group row">
                        <div class="col-sm-2"><fmt:message key="user.lastName"/></div>
                        <div class="col-sm-6">${account.lastName}</div>
                    </div>
                    <div class="form-group row">
                        <div class="col-sm-2"><fmt:message key="user.email"/></div>
                        <div class="col-sm-6">
                            <a href="mailto:${account.email}">${account.email}</a>
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="col-sm-2"><fmt:message key="user.phone"/></div>
                        <div class="col-sm-6">
                            <span>${account.phone}</span>
                        </div>
                    </div>
                </div>
                <div>
                    <div class="form-group row">
                        <div class="col-sm-2"><fmt:message key="user.enable"/></div>
                        <div class="col-sm-6"><c:out value="${account.enabled}"/></div>
                    </div>
                    <div class="form-group row">
                        <div class="col-sm-2"><fmt:message key="user.roles"/></div>
                        <div class="col-sm-6">
                            <c:forEach var="role" items="${account.roles}">
                                <c:out value="${role.code}"/><br/>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
            <a href="/library/admin/manage/${account.id}/edit" class="btn btn-primary"><fmt:message
                    key="user.edit"/></a>
        </div>
    </jsp:body>
</tag:authorization>