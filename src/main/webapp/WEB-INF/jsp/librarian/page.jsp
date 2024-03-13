<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>

<fmt:requestEncoding value="UTF-8"/>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="interface"/>

<fmt:message var="dateCreated" key="librarian.user.order.date.created"/>
<fmt:message var="dateExpire" key="librarian.user.order.date.expire"/>

<tag:authorization>
    <jsp:attribute name="head">
        <title>
            Library
        </title>
        <script src="${pageContext.request.contextPath}/js/pagination.js"></script>
        <script src="${pageContext.request.contextPath}/js/pageable-librarian-orders.js"></script>
    </jsp:attribute>
    <jsp:body>
        <div id="label_ISBN" hidden="hidden"><fmt:message key="order.label.ISBN"/></div>
        <div id="date_order" hidden="hidden"><fmt:message key="order.label.date_created"/></div>
        <div id="place" hidden="hidden"><fmt:message key="order.label.place"/></div>
        <div id="status" hidden="hidden"><fmt:message key="order.label.status"/></div>
        <div id="date_expires" hidden="hidden"><fmt:message key="order.label.date_expire"/></div>
        <div id="fine" hidden="hidden"><fmt:message key="order.label.fine"/></div>
        <div id="edit_order" hidden="hidden"><fmt:message key="order.label.edit"/></div>
        <div class="container main-content">
            <ol class="breadcrumb list-group-item-dark rounded">
                <li class="breadcrumb-item active"><a href="/"><fmt:message key="home.pageTitle"/></a></li>
            </ol>
            <h1>
                <fmt:message key="orderList.pageTitle"/>
            </h1>
            <div class="grid-container">
                <div class="grid-left-3">
                    <div class="list-group list-group-flush components">
                        <label class="list-group-item list-group-item-action" onclick="showUsersOrders()">
                            <fmt:message key="librarian.orders"/>
                        </label>
                        <label class="list-group-item list-group-item-action"
                               onclick="showAcceptedOrdersOnSubscription()">
                            <fmt:message key="librarian.subscriptions"/>
                        </label>
                        <label class="list-group-item list-group-item-action" onclick="showAcceptedOrdersReadingRoom()">
                            <fmt:message key="librarian.room"/>
                        </label>
                    </div>
                </div>
                <div class="grid-right-9">
                    <div class="form-group">
                        <input id="search" class="col-4 rounded border" type="text"
                               placeholder="<fmt:message key="table.search" />">
                        <input id="size" class="col-2 rounded border" type="number" min="2" max="8" value="5">
                        <input name="sorting" class="hidden" type="radio" id="asc" value="ASC" checked><label
                            class="col-2"
                            for="asc"><fmt:message
                            key="table.asc"/></label>
                        <input name="sorting" class="hidden" type="radio" id="desc" value="DESC"><label class="col-2"
                                                                                                        for="desc"><fmt:message
                            key="table.desc"/></label>
                        <div id="page-navigation" class="pagination"></div>
                    </div>
                    <table class="table table-bordered table-active table-hover table-striped">
                        <thead class="table-header table-dark">
                        <tr>
                            <th id="login" scope="col"><fmt:message key="librarian.user.login"/></th>
                            <th id="book_name" scope="col"><fmt:message key="orderList.table.name"/></th>
                            <th id="date_created" scope="col">${dateCreated}</th>
                            <th id="date_expire" scope="col">${dateExpire}</th>
                            <th id="isbn" scope="col"><fmt:message key="orderList.table.ISBN"/></th>
                        </tr>
                        </thead>
                        <tbody id="pageable-list">
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </jsp:body>
</tag:authorization>
