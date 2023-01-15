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
      <script src="${pageContext.request.contextPath}/js/pageable-authors-book.js"></script>
        <script src="${pageContext.request.contextPath}/js/pagination.js"></script>
    </jsp:attribute>
    <jsp:body>
        <div class="container main-content">
            <ol class="breadcrumb list-group-item-dark rounded">
                <li class="breadcrumb-item"><a href="/"><fmt:message key="home.pageTitle"/></a></li>
                <li class="breadcrumb-item active"><a href="/library/admin/books"><fmt:message
                        key="bookList.pageTitle"/></a></li>
            </ol>
            <h1><fmt:message key="editBook.pageTitle"/></h1>
            <form id="add-book-form" data-toggle="validator"
                  method="post">
                <div class="form-group">
                    <input type="text" class="form-control caps" name="name"
                           placeholder="<fmt:message key="newBook.label.name" />" required value="${book.name}">
                </div>
                <div class="form-group">
                    <label class="control-label"><fmt:message key="newBook.label.count"/></label>
                    <input class="col-2 rounded border" type="number" min="0" required name="count" value="${book.count}">
                </div>
                <div class="form-group">
                    <label class="control-label"><fmt:message key="newBook.label.ISBN"/></label>
                    <input class="col-2 rounded border" type="number" min="0000000000000" minlength="13"
                           maxlength="13" placeholder="0000000000000" required name="ISBN" value="${book.ISBN}">
                </div>
                <div class="form-group">
                    <label class="control-label"><fmt:message key="newBook.label.publicationDate"/></label>
                    <input class="col-2 rounded border" type="date" required name="publicationDate">
                        ${book.publicationDate}
                </div>
                <div class="form-group">
                    <label class="control-label"><fmt:message key="newBook.label.fine"/></label>
                    <input class="col-2 rounded border" type="number" step="0.01" min="0" required name="fine" value="${book.fine}">
                </div>
                <div class="form-group">
                    <label class="control-label"><fmt:message key="newBook.label.language"/></label>
                    <input type="radio" name="language" value="ENGLISH" required checked><fmt:message
                        key="newBook.label.english"/>
                    <input type="radio" name="language" value="UKRAINIAN" required><fmt:message
                        key="newBook.label.ukrainian"/>
                </div>
                <div class="grid-container">
                    <div class="grid-left-3">
                        <div class="form-group">
                            <label class="control-label"><fmt:message key="newBook.label.authors"/></label>
                            <ol id="authors-list">
                                <c:forEach items="${book.authors}" var="author">
                                    <li id="${author.id}">
                                        ${author.firstName} ${author.lastName}
                                    </li>
                                </c:forEach>
                            </ol>
                        </div>
                    </div>

                    <div class="grid-right-9">
                        <input id="search" class="col-4 rounded border" type="text"
                               placeholder="<fmt:message key="table.search" />">
                        <input id="size" class="col-2 rounded border" type="number" min="2" max="8" value="5">
                        <input name="sorting" class="hidden" type="radio" id="asc" value="asc" checked><label
                            class="col-2"
                            for="asc"><fmt:message
                            key="table.asc"/></label>
                        <input name="sorting" class="hidden" type="radio" id="desc" value="desc"><label
                            class="col-2"
                            for="desc"><fmt:message
                            key="table.desc"/></label>
                        <div id="page-navigation" class="pagination"></div>
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
                <div class="form-group">
                    <button type="submit" id="form-submit" class="btn btn-primary" onclick="addAuthors()">
                        <span><fmt:message key="newRequest.label.submit"/></span>
                    </button>
                </div>
                <div class="form-group">
                    <label class="btn btn-danger" onclick="location.href='/library/admin/books'">
                        <fmt:message key="newRequest.label.close"/>
                    </label>
                </div>
            </form>
            <script>
                $(document).ready(function () {
                    $('#add-book-form').submit(function (e) {
                        var authors = $('#authors-list').children();
                        if (authors.length === 0) {
                            alert("<fmt:message key="newBook.alert.authors"/>");
                            e.preventDefault();
                        }
                    });
                });
            </script>

        </div>

    </jsp:body>
</tag:authorization>