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
        <script src="${pageContext.request.contextPath}/js/pageable-books-admin.js"></script>
        <script src="${pageContext.request.contextPath}/js/pagination.js"></script>
    </jsp:attribute>
    <jsp:body>
        <div id="book_count" class="hidden"><fmt:message key="book.count"/></div>
        <div id="book_ISBN" class="hidden"><fmt:message key="book.ISBN"/></div>
        <div id="book_date_published" class="hidden"><fmt:message key="book.publicationDate"/></div>
        <div id="book_fine" class="hidden"><fmt:message key="book.fine"/></div>
        <div id="book_language" class="hidden"><fmt:message key="book.language"/></div>
        <div id="book_location" class="hidden"><fmt:message key="book.location"/></div>
        <div id="book_authors_label" class="hidden"><fmt:message key="book.authors"/></div>
        <div id="more_info" class="hidden"><fmt:message key="book.moreInfo"/></div>
        <div class="container main-content">
            <ol class="breadcrumb list-group-item-dark rounded">
                <li class="breadcrumb-item active"><a href="/"><fmt:message key="home.pageTitle"/></a></li>
            </ol>
            <c:if test="${param.created == 'true'}">
                <div class="alert alert-success" role="alert">
                    <fmt:message key="book.created"/>
                </div>
            </c:if>
            <c:if test="${param.created == 'false'}">
                <div class="alert alert-danger" role="alert">
                    <fmt:message key="book.notCreated"/> ${param.message}
                </div>
            </c:if>
            <c:if test="${param.updated == 'true'}">
                <div class="alert alert-success" role="alert">
                    <fmt:message key="book.updated"/>
                </div>
            </c:if>
            <c:if test="${param.updated == 'false'}">
                <div class="alert alert-danger" role="alert">
                    <fmt:message key="book.notUpdated"/>
                </div>
            </c:if>
            <c:if test="${param.deleted == 'true'}">
                <div class="alert alert-success" role="alert">
                    <fmt:message key="book.deleted"/>
                </div>
            </c:if>
            <c:if test="${param.deleted == 'false'}">
                <div class="alert alert-danger" role="alert">
                    <fmt:message key="book.notDeleted"/>
                </div>
            </c:if>
            <h1>
                <fmt:message key="admin.pageTitle"/>
            </h1>
            <div class="grid-container">
                <div class="grid-left-3">
                    <div class="list-group list-group-flush components">
                        <a class="btn-lg library-create-book" href="/library/books/admin/create">
                            <fmt:message key="admin.create.book"/>
                        </a>
                        <label class="list-group-item list-group-item-action">
                            <fmt:message key="admin.list.books"/>
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
                            <th id="book_title" scope="col"><fmt:message key="orderList.table.title"/></th>
                            <th id="isbn" scope="col"><fmt:message key="orderList.table.ISBN"/></th>
                            <th id="date_publication" scope="col"><fmt:message
                                    key="orderList.table.date_publication"/></th>
                            <th id="book_authors" scope="col"><fmt:message key="orderList.table.authors"/></th>
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