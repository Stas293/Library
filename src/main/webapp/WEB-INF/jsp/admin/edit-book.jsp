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
                <div class="mb-3">
                    <label for="bookName">
                        <fmt:message key="newBook.label.name"/>
                    </label>
                    <c:set var="bookName">${book.name}</c:set>
                    <c:if test="${not empty bookName}">
                        <c:set var="bookTitle">${book.name}</c:set>
                    </c:if>
                    <input name="bookName" id="bookName" class="form-control" value=${bookTitle}>
                    <c:set var="bookNameErrors">${bookValidationError.name}</c:set>
                    <c:if test="${not empty bookNameErrors}">
                        <div class="alert alert-danger"><fmt:message key="${bookValidationError.name}"/></div>
                    </c:if>
                </div>
                <div class="mb-3">
                    <label for="bookCount">
                        <fmt:message key="newBook.label.count"/>
                    </label>
                    <c:set var="bookCount">${book.count}</c:set>
                    <c:if test="${not empty bookCount}">
                        <c:set var="bookNumber">${book.count}</c:set>
                    </c:if>
                    <input class="col-2 rounded border" id="bookCount" type="number" min="0" required name="count"
                           value=${bookNumber}>
                    <c:set var="bookNameErrors">${bookValidationError.count}</c:set>
                    <c:if test="${not empty bookNameErrors}">
                        <div class="alert alert-danger"><fmt:message key="${bookValidationError.count}"/></div>
                    </c:if>
                </div>
                <div class="mb-3">
                    <label for="bookISBN">
                        <fmt:message key="newBook.label.ISBN"/>
                    </label>
                    <c:set var="bookISBN">${book.ISBN}</c:set>
                    <c:if test="${not empty bookISBN}">
                        <c:set var="bookISBNNumber">${book.ISBN}</c:set>
                    </c:if>
                    <input class="col-2 rounded border" id="bookISBN" type="number" min="0000000000000" minlength="13"
                           maxlength="13" placeholder="0000000000000" required name="ISBN" value=${bookISBNNumber}>
                    <c:set var="bookNameErrors">${bookValidationError.ISBN}</c:set>
                    <c:if test="${not empty bookNameErrors}">
                        <div class="alert alert-danger"><fmt:message key="${bookValidationError.ISBN}"/></div>
                    </c:if>
                </div>
                <div class="mb-3">
                    <label for="publicationDate">
                        <fmt:message key="newBook.label.publicationDate"/>
                    </label>
                    <c:set var="bookPublicationDate">${book.publicationDate}</c:set>
                    <c:if test="${not empty bookPublicationDate}">
                        <c:set var="bookPublicationDateNumber">${book.publicationDate}</c:set>
                    </c:if>
                    <input class="col-2 rounded border" id="publicationDate" type="date" required name="publicationDate"
                           value=${bookPublicationDateNumber}>
                    <c:set var="bookNameErrors">${bookValidationError.datePublication}</c:set>
                    <c:if test="${not empty bookNameErrors}">
                        <div class="alert alert-danger"><fmt:message
                                key="${bookValidationError.datePublication}"/></div>
                    </c:if>
                </div>
                <div class="mb-3">
                    <label for="bookFine">
                        <fmt:message key="newBook.label.fine"/>
                    </label>
                    <c:set var="bookFine">${book.fine}</c:set>
                    <c:if test="${not empty bookFine}">
                        <c:set var="bookFineNumber">${book.fine}</c:set>
                    </c:if>
                    <input class="col-2 rounded border" id="bookFine" type="number" min="0" required name="fine"
                           value=${bookFineNumber}>
                    <c:set var="bookNameErrors">${bookValidationError.finePerDay}</c:set>
                    <c:if test="${not empty bookNameErrors}">
                        <div class="alert alert-danger"><fmt:message key="${bookValidationError.finePerDay}"/></div>
                    </c:if>
                </div>
                <div class="mb-3">
                    <label class="control-label"><fmt:message key="newBook.label.language"/></label>
                    <input type="radio" name="language" value="ENGLISH" required checked><fmt:message
                        key="newBook.label.english"/>
                    <input type="radio" name="language" value="UKRAINIAN" required><fmt:message
                        key="newBook.label.ukrainian"/>
                </div>
                <c:set var="bookAuthorsErrors">${bookValidationError.authors}</c:set>
                <c:if test="${not empty bookAuthorsErrors}">
                    <div class="alert alert-danger"><fmt:message key="${bookValidationError.authors}"/></div>
                </c:if>
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
                $(document).ready(function () {
                    document.getElementById("publicationDate").max = new Date().toISOString().split("T")[0];
                });
            </script>

        </div>

    </jsp:body>
</tag:authorization>