<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>

<c:url var="userUrl" value="library/admin/users/${account.id}"/>

<fmt:requestEncoding value="UTF-8"/>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="interface"/>

<tag:authorization>
    <jsp:attribute name="head">
        <title><fmt:message key="editUser.pageTitle"/></title>
    </jsp:attribute>
    <jsp:body>
        <div class="container main-content">
            <ol class="breadcrumb list-group-item-dark rounded">
                <li class="breadcrumb-item"><a href="/"><fmt:message key="home.pageTitle"/></a></li>
                <li class="breadcrumb-item"><a href="/library/admin/page"><fmt:message key="usersList.pageTitle"/></a>
                </li>
                <li class="breadcrumb-item active"><a href="${userUrl}">${account.login}</a></li>
            </ol>
            <h1><fmt:message key="editUser.pageTitle"/>: ${account.login} </h1>

            <form class="main" method="post">
                <input type="hidden" name="_method" value="PUT"/>
                <input type="hidden" name="id" value="${account.id}"/>
                <div class="form-group">
                    <label>
                            ${account.firstName} ${account.lastName}
                    </label>
                </div>

                <div class="form-group">
                    <label>
                        <a href="mailto:${account.email}">${account.email}</a>
                    </label>
                </div>
                <div class="form-group">
                    <label>
                            ${account.phone}
                    </label>
                </div>
                <div class="form-group">
                    <label for="role" class="col-form-label-lg">
                        <fmt:message key="editUser.label.roles"/>
                    </label>
                    <select required multiple name="role" class="form-control" id="role">
                        <c:forEach items="${rolesList}" var="rolesList">
                            <c:set var="isPresent" value="false"/>
                            <c:forEach items="${account.roles}" var="role">
                                <c:if test="${role.code eq rolesList.code}">
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