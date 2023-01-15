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
        <script src="${pageContext.request.contextPath}/js/pageable-authors-admin.js"></script>
        <script src="${pageContext.request.contextPath}/js/pagination.js"></script>
    </jsp:attribute>
    <jsp:body>
        <div id="edit_author_button" hidden="hidden"><fmt:message key="admin.edit.author.button"/></div>
        <div id="delete_author_button" hidden="hidden"><fmt:message key="admin.delete.author.button"/></div>
        <div class="container main-content">
            <ol class="breadcrumb list-group-item-dark rounded">
                <li class="breadcrumb-item active"><a href="/"><fmt:message key="home.pageTitle"/></a></li>
            </ol>
            <c:if test="${param.created == 'true'}">
                <div class="alert alert-success" role="alert">
                    <fmt:message key="author.created"/>
                </div>
            </c:if>
            <c:if test="${param.created == 'false'}">
                <div class="alert alert-danger" role="alert">
                    <fmt:message key="author.notCreated"/>
                </div>
            </c:if>
            <c:if test="${param.updated == 'true'}">
                <div class="alert alert-success" role="alert">
                    <fmt:message key="author.updated"/>
                </div>
            </c:if>
            <c:if test="${param.updated == 'false'}">
                <div class="alert alert-danger" role="alert">
                    <fmt:message key="author.notUpdated"/>
                </div>
            </c:if>
            <c:if test="${param.deleted == 'true'}">
                <div class="alert alert-success" role="alert">
                    <fmt:message key="author.deleted"/>
                </div>
            </c:if>
            <c:if test="${param.deleted == 'false'}">
                <div class="alert alert-danger" role="alert">
                    <fmt:message key="author.notDeleted"/>
                </div>
            </c:if>
            <input type="checkbox" class="input-modal-window" id="new-book-window">
            <div class="modal hidden-new-book-window">
                <div class="center">
                    <div class="book-create-form">
                        <form id="requestForm" data-toggle="validator" novalidate action="/library/admin/new-author">
                            <h1><fmt:message key="newAuthor.pageTitle"/></h1>
                            <div class="form-group">
                                <input type="text" class="form-control caps" name="firstName"
                                       placeholder="<fmt:message key="newAuthor.first_name" />" required>
                            </div>
                            <div class="form-group">
                                <input type="text" class="form-control caps" name="lastName"
                                       placeholder="<fmt:message key="newAuthor.last_name" />" required>
                            </div>
                            <div class="form-group">
                                <button type="submit" id="form-submit" class="btn btn-primary">
                                    <span><fmt:message key="newRequest.label.submit"/></span>
                                </button>
                            </div>
                            <div class="form-group">
                                <label class="btn btn-danger" for="new-book-window">
                                    <fmt:message key="newRequest.label.close"/>
                                </label>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <h1>
                <fmt:message key="authorList.pageTitle"/>
            </h1>
            <div class="grid-container">
                <div class="grid-left-3">
                    <div class="list-group list-group-flush components">
                        <label class="library-create-book btn-lg" for="new-book-window">
                            <fmt:message key="admin.create.author"/>
                        </label>
                        <label class="list-group-item list-group-item-action">
                            <fmt:message key="admin.list.authors"/>
                        </label>
                    </div>
                </div>
                <div class="grid-right-9">
                    <div class="form-group">
                        <input id="search" class="col-4 rounded border" type="text"
                               placeholder="<fmt:message key="table.search" />">
                        <input id="size" class="col-2 rounded border" type="number" min="2" max="8" value="5">
                        <input name="sorting" class="hidden" type="radio" id="asc" value="asc" checked><label
                            class="col-2"
                            for="asc"><fmt:message
                            key="table.asc"/></label>
                        <input name="sorting" class="hidden" type="radio" id="desc" value="desc"><label class="col-2"
                                                                                                        for="desc"><fmt:message
                            key="table.desc"/></label>
                        <div id="page-navigation" class="pagination"></div>
                    </div>
                    <table class="table table-active table-hover table-active table-hover table-striped">
                        <thead class="table-header table-dark">
                        <tr>
                            <th id="author_id" scope="col"><fmt:message key="newBook.label.author"/></th>
                            <th id="author_first_name" scope="col"><fmt:message
                                    key="newBook.table.first_name"/></th>
                            <th id="author_last_name" scope="col"><fmt:message
                                    key="newBook.table.last_name"/></th>
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