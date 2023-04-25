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
        <script src="${pageContext.request.contextPath}/js/pageable-book-user-onload.js"></script>
        <script src="${pageContext.request.contextPath}/js/pagination.js"></script>
    </jsp:attribute>
    <jsp:body>
        <div id="label_ISBN" hidden="hidden"><fmt:message key="orderList.table.ISBN"/></div>
        <div id="label_date_publication" hidden="hidden"><fmt:message key="orderList.table.date_publication"/></div>
        <div id="label_authors" hidden="hidden"><fmt:message key="orderList.table.authors"/></div>
        <div id="label_book_title" hidden="hidden"><fmt:message key="orderList.table.title"/></div>
        <div id="label_date_created" hidden="hidden"><fmt:message key="orderList.table.dateCreated"/></div>
        <div id="label_date_expire" hidden="hidden"><fmt:message key="orderList.table.dateExpire"/></div>
        <div id="label_place" hidden="hidden"><fmt:message key="orderList.table.place"/></div>
        <div id="label_fine" hidden="hidden"><fmt:message key="orderList.table.fine"/></div>
        <div class="container main-content">
            <ol class="breadcrumb list-group-item-dark rounded">
                <li class="breadcrumb-item active"><a href="/"><fmt:message key="home.pageTitle"/></a></li>
            </ol>
            <c:if test="${param.created == true}">
                <div class="alert alert-success"><fmt:message key="order.create.message" /></div>
            </c:if>
            <c:if test="${param.created == false}">
                <div class="alert alert-danger"><fmt:message key="order.create.error" /></div>
            </c:if>
            <h1>
                <fmt:message key="orderList.pageTitle"/>
            </h1>

            <div class="grid-container">
                <div class="grid-left-3">
                    <div class="list-group list-group-flush components">
                        <label class="list-group-item list-group-item-action" onclick="listOrderToChoose()">
                            <fmt:message key="order.choose.book"/>
                        </label>
                        <label class="list-group-item list-group-item-action" onclick="listRegisteredOrders()">
                            <fmt:message key="order.registered"/>
                        </label>
                        <label class="list-group-item list-group-item-action" onclick="listAcceptedOrder()">
                            <fmt:message key="order.current"/>
                        </label>
                    </div>
                </div>
                <div class="grid-right-9">
                    <div class="form-group">
                        <input id="search" class="col-4 rounded border" type="text"
                               placeholder="<fmt:message key="table.search" />">
                        <input id="size" class="col-2 rounded border" type="number" min="2" max="8" value="5">
                        <input name="sorting" class="hidden" type="radio" id="asc" value="ASC" checked><label class="col-2"
                                                                                                              for="asc"><fmt:message
                            key="table.asc"/></label>
                        <input name="sorting" class="hidden" type="radio" id="desc" value="DESC"><label class="col-2"
                                                                                                        for="desc"><fmt:message
                            key="table.desc"/></label>
                        <div id="page-navigation" class="pagination"></div>
                    </div>
                    <table class="table table-bordered table-active table-hover table-striped">
                        <thead class="table-header table-dark ">
                        <tr>
                            <th id="book_title" scope="col"><fmt:message key="orderList.table.title"/></th>
                            <th id="isbn" scope="col"><fmt:message key="orderList.table.ISBN"/></th>
                            <th id="date_created" scope="col"><fmt:message key="orderList.table.dateCreated"/></th>
                            <th id="date_publication" scope="col"><fmt:message
                                    key="orderList.table.date_publication"/></th>
                            <th id="place" scope="col"><fmt:message key="orderList.table.place"/></th>
                            <th id="date_expire" scope="col"><fmt:message key="orderList.table.dateExpire"/></th>
                            <th id="book_authors" scope="col"><fmt:message key="orderList.table.authors"/></th>
                            <th id="fine" scope="col"><fmt:message key="orderList.table.fine"/></th>
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