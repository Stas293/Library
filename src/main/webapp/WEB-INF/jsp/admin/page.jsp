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
        <title>
            Library
        </title>
      <script src="${pageContext.request.contextPath}/js/pagination.js"></script>
      <script src="${pageContext.request.contextPath}/js/pageable-admin-users.js"></script>
    </jsp:attribute>
    <jsp:body>
        <div class="container main-content">
            <ol class="breadcrumb list-group-item-dark rounded">
                <li class="breadcrumb-item active"><a href="/"><fmt:message key="home.pageTitle"/></a></li>
            </ol>
            <c:if test="${param.saved == true}">
                <div class="alert alert-success" role="alert"><fmt:message key="user.save"/></div>
            </c:if>
            <c:if test="${param.deleted == true}">
                <div class="alert alert-success" role="alert"><fmt:message key="user.delete.true"/></div>
            </c:if>
            <c:if test="${param.deleted == false}">
                <div class="alert alert-danger" role="alert"><fmt:message key="user.delete.false"/></div>
            </c:if>
            <h1><fmt:message key="usersList.pageTitle"/></h1>
            <input id="search" class="col-4 rounded border" type="text"
                   placeholder="<fmt:message key="table.search" />">
            <input id="size" class="col-2 rounded border" type="number" min="2" max="8" value="5">
            <input name="sorting" class="hidden" type="radio" id="asc" value="ASC" checked><label class="col-2"
                                                                                                  for="asc"><fmt:message
                key="table.asc"/></label>
            <input name="sorting" class="hidden" type="radio" id="desc" value="DESC"><label class="col-2"
                                                                                            for="desc"><fmt:message
                key="table.desc"/></label>
            <div id="page-navigation"></div>
            <table class="table table-bordered table-active table-hover table-striped">
                <thead class="table-header table-dark">
                <tr>
                    <th id="login" class="string"><fmt:message key="usersList.table.login"/></th>
                    <th id="first_name" class="string"><fmt:message key="usersList.table.firstName"/></th>
                    <th id="last_name" class="string"><fmt:message key="usersList.table.lastName"/></th>
                    <th id="phone" class="string"><fmt:message key="usersList.table.phone"/></th>
                </tr>
                </thead>
                <tbody id="pageable-list">
                </tbody>
            </table>
        </div>
    </jsp:body>
</tag:authorization>
