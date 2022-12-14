<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>

<c:url var="userUrl" value="/library/admin/accounts/${sessionScope.account.id}"/>

<fmt:requestEncoding value="UTF-8"/>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="interface"/>

<tag:authorization>
    <jsp:attribute name="head">
        <title><fmt:message key="editUser.pageTitle"/></title>
    </jsp:attribute>
    <jsp:body>
        <div class="container main-content">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="/"><fmt:message key="home.pageTitle"/></a></li>
                <li class="breadcrumb-item"><a href="/library/admin/page"><fmt:message key="usersList.pageTitle"/></a></li>
                <li class="breadcrumb-item active"><a href="${userUrl}">${sessionScope.account.login}</a></li>
            </ol>
            <h1><fmt:message key="editUser.pageTitle"/>: ${sessionScope.account.login} </h1>

            <c:if test="${param.saved == true}">
                <div class="info alert"><fmt:message key="editUser.user.save"/><a href="${userUrl}"><fmt:message
                        key="editUser.user.view"/></a></div>
            </c:if>

            <form class="main" method="post">
                <div class="form-group">
                    <label>
                            ${sessionScope.account.firstName} ${sessionScope.account.lastName}
                    </label>
                </div>

                <div class="form-group">
                    <label>
                        <a href="mailto:${sessionScope.account.email}">${sessionScope.account.email}</a>
                    </label>
                </div>
                <div class="form-group">
                    <label>
                            ${sessionScope.account.phone}
                    </label>
                </div>
                <div class="form-group">
                    <label for="role" class="col-form-label-lg">
                        <fmt:message key="editUser.label.roles"/>
                    </label>
                    <select required multiple name="role" class="form-control" id="role">
                        <c:forEach items="${sessionScope.rolesList}" var="rolesList">
                            <c:set var="isPresent" value="false"/>
                            <c:forEach items="${sessionScope.roles}" var="role">
                                <c:if test="${role eq rolesList.code}">
                                    <option value="${rolesList.code}" selected>${rolesList.code}</option>
                                    <c:set var="isPresent" value="true"/>
                                </c:if>
                            </c:forEach>
                            <c:if test="${isPresent eq 'false'}">
                                <option value="${rolesList.code}">${rolesList.code}</option>
                            </c:if>
                        </c:forEach>
                    </select>
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