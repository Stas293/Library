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
        <script src="${pageContext.request.contextPath}/js/pageable-history-onload.js"></script>
    </jsp:attribute>
    <jsp:body>
        <div class="container main-content">
            <ol class="breadcrumb" >
                <li class="breadcrumb-item active"><a href="/"><fmt:message key="home.pageTitle"/></a></li>
            </ol>
            <h1>
                <fmt:message key="historyOrder.pageTitle"/>
            </h1>
            <div class="form-group">
                <input id="search" class="col-4 rounded border" type="text"
                       placeholder="<fmt:message key="table.search" />">
                <input id="size" class="col-2 rounded border" type="number" min="2" max="8" value="5">
                <input name="sorting" class="hidden" type="radio" id="asc" value="asc" checked><label class="col-2"
                                                                                                      for="asc"><fmt:message
                    key="table.asc"/></label>
                <input name="sorting" class="hidden" type="radio" id="desc" value="desc"><label class="col-2"
                                                                                                for="desc"><fmt:message
                    key="table.desc"/></label>
                <div id="page-navigation" class="pagination"></div>
            </div>
            <table class="table table-bordered table-active table-hover table-striped">
                <thead class="table-header table-dark ">
                <tr>
                    <th id = "book_name" scope="col"><fmt:message key="orderList.table.name"/></th>
                    <th id = "date_created" scope="col"><fmt:message key="orderList.table.date_created"/></th>
                    <th id = "date_returned" scope="col"><fmt:message key="orderList.table.date_returned"/></th>
                    <th id = "status" scope="col"><fmt:message key="orderList.table.status"/></th>
                </tr>
                </thead>
                <tbody id="pageable-list">
                </tbody>
            </table>

        </div>
    </jsp:body>
</tag:authorization>