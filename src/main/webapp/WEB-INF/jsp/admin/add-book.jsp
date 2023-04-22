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
        <link rel="stylesheet" href="//code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">
        <script src="https://code.jquery.com/ui/1.13.2/jquery-ui.js"></script>
    </jsp:attribute>
    <jsp:body>
        <div class="container main-content">
            <ol class="breadcrumb list-group-item-dark rounded">
                <li class="breadcrumb-item"><a href="/"><fmt:message key="home.pageTitle"/></a></li>
                <li class="breadcrumb-item active"><a href="/library/books/admin/page"><fmt:message
                        key="bookList.pageTitle"/></a></li>
            </ol>
            <h1><fmt:message key="newBook.pageTitle"/></h1>
            <div class="row">
                <div class="col-12">
                    <div class="alert alert-success" id="success-alert" style="display: none">
                        <button type="button" class="btn btn-close" data-dismiss="alert"></button>
                        <strong><fmt:message key="newBook.label.success"/></strong>
                        <span id="success-message">
                            <fmt:message key="newBook.label.successMessage"/>
                        </span>
                    </div>
                </div>
            </div>

            <div class="modal fade" id="createKeywordModal" tabindex="-1" aria-labelledby="createKeywordModalLabel"
                 aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <form id="createKeywordForm">
                            <div class="modal-header">
                                <h5 class="modal-title" id="createKeywordModalLabel"><fmt:message
                                        key="newBook.label.createKeyword"/></h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal"
                                        aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <div class="mb-3">
                                    <label for="newKeywordInput" class="form-label"><fmt:message
                                            key="newBook.label.keyword"/></label>
                                    <input type="text" class="form-control" id="newKeywordInput" required>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal"><fmt:message
                                        key="newBook.label.close"/></button>
                                <button type="submit" class="btn btn-primary"><fmt:message
                                        key="newBook.label.save"/></button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <div class="modal fade" id="createAuthorModal" tabindex="-1" aria-labelledby="createKeywordModalLabel"
                 aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <form id="createAuthorForm">
                            <div class="modal-header">
                                <h5 class="modal-title" id="createAuthorModalLabel"><fmt:message
                                        key="newBook.label.createAuthor"/></h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal"
                                        aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <div class="mb-3">
                                    <label for="newAuthorFirstNameInput" class="form-label"><fmt:message
                                            key="newBook.label.firstName"/></label>
                                    <input type="text" class="form-control" id="newAuthorFirstNameInput" required>
                                    <div class="alert alert-danger" id="first-name-error">
                                        <fmt:message key="form.validation.name"/>
                                    </div>
                                </div>
                                <div class="mb-3">
                                    <label for="newAuthorMiddleNameInput" class="form-label"><fmt:message
                                            key="newBook.label.middleName"/></label>
                                    <input type="text" class="form-control" id="newAuthorMiddleNameInput">
                                    <div class="alert alert-danger" id="middle-name-error">
                                        <fmt:message key="form.validation.name"/>
                                    </div>
                                </div>
                                <div class="mb-3">
                                    <label for="newAuthorLastNameInput" class="form-label"><fmt:message
                                            key="newBook.label.lastName"/></label>
                                    <input type="text" class="form-control" id="newAuthorLastNameInput" required>
                                    <div class="alert alert-danger" id="last-name-error">
                                        <fmt:message key="form.validation.name"/>
                                    </div>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal"><fmt:message
                                        key="newBook.label.close"/></button>
                                <button type="submit" class="btn btn-primary"><fmt:message
                                        key="newBook.label.save"/></button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <form id="add-book-form" data-toggle="validator" action="/library/books/admin"
                  method="post">
                <div class="mb-3">
                    <label for="title">
                        <fmt:message key="newBook.label.title"/>
                    </label>
                    <c:set var="title">${book.title}</c:set>
                    <c:if test="${not empty title}">
                        <c:set var="title">${book.title}</c:set>
                    </c:if>
                    <input name="title" id="title" class="form-control" required value=${title}>
                    <c:set var="bookNameErrors">${bookValidationError.title}</c:set>
                    <c:if test="${not empty bookNameErrors}">
                        <div class="alert alert-danger"><fmt:message key="${bookValidationError.title}"/></div>
                    </c:if>
                </div>
                <div class="mb-3">
                    <label for="description">
                        <fmt:message key="newBook.label.description"/>
                    </label>
                    <c:set var="description">${book.description}</c:set>
                    <c:if test="${not empty description}">
                        <c:set var="description">${book.description}</c:set>
                    </c:if>
                    <textarea name="description" id="description" class="form-control">${description}</textarea>
                    <c:set var="bookNameErrors">${bookValidationError.description}</c:set>
                    <c:if test="${not empty bookNameErrors}">
                        <div class="alert alert-danger"><fmt:message key="${bookValidationError.description}"/></div>
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
                    <c:set var="bookISBN">${book.isbn}</c:set>
                    <c:if test="${not empty bookISBN}">
                        <c:set var="bookISBNNumber">${book.isbn}</c:set>
                    </c:if>
                    <input class="col-2 rounded border" id="bookISBN" type="text" min="0000000000000"
                           placeholder="0000000000000" required name="isbn" pattern="\d{13}" value=${bookISBNNumber}>
                    <c:set var="bookNameErrors">${bookValidationError.isbn}</c:set>
                    <c:if test="${not empty bookNameErrors}">
                        <div class="alert alert-danger"><fmt:message key="${bookValidationError.isbn}"/></div>
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
                    <c:set var="bookNameErrors">${bookValidationError.publicationDate}</c:set>
                    <c:if test="${not empty bookNameErrors}">
                        <div class="alert alert-danger"><fmt:message
                                key="${bookValidationError.publicationDate}"/></div>
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
                    <input class="col-2 rounded border" id="bookFine" type="number" step="0.01" min="0" required name="fine"
                           value=${bookFineNumber}>
                    <c:set var="bookNameErrors">${bookValidationError.fine}</c:set>
                    <c:if test="${not empty bookNameErrors}">
                        <div class="alert alert-danger"><fmt:message key="${bookValidationError.fine}"/></div>
                    </c:if>
                </div>
                <div class="mb-3">
                    <label class="control-label"><fmt:message key="newBook.label.language"/></label>
                    <c:set var="bookLanguage" value="${book.language}"/>
                    <input type="radio" name="language" value="English"
                           <c:if test="${bookLanguage == 'English'}">checked</c:if> required>
                    <fmt:message key="newBook.label.english"/>
                    <input type="radio" name="language" value="Українська"
                           <c:if test="${bookLanguage == 'Українська'}">checked</c:if> required>
                    <fmt:message key="newBook.label.ukrainian"/>
                </div>
                <div class="mb-3">
                    <label for="location">
                        <fmt:message key="newBook.label.location"/>
                    </label>
                    <c:set var="location">${book.location}</c:set>
                    <c:if test="${not empty location}">
                        <c:set var="location">${book.location}</c:set>
                    </c:if>
                    <input name="location" id="location" class="form-control" required value="${location}">
                    <c:set var="bookNameErrors">${bookValidationError.location}</c:set>
                    <c:if test="${not empty bookNameErrors}">
                        <div class="alert alert-danger"><fmt:message key="${bookValidationError.location}"/></div>
                    </c:if>
                </div>
                <div class="mb-3">
                    <label for="author-choices" class="form-label"><fmt:message key="newBook.label.authors"/></label>
                    <input type="text" class="form-control" id="author-choices" placeholder="<fmt:message
                            key="newBook.label.enter_authors"/>">
                    <ul id="author-list">
                        <c:forEach items="${book.authors}" var="author">
                            <li data-id="${author.id}">
                                <c:if test="${not empty author.middleName}">
                                    ${author.firstName} ${author.middleName} ${author.lastName}
                                </c:if>
                                <c:if test="${empty author.middleName}">
                                    ${author.firstName} ${author.lastName}
                                </c:if>
                                <button class='btn btn-close btn-sm' aria-label='Close'></button>
                            </li>
                        </c:forEach>
                    </ul>
                    <input type="hidden" name="authors" id="authors-input">
                </div>
                <div class="mb-3">
                    <label for="keyword-choices" class="form-label"><fmt:message key="newBook.label.keywords"/></label>
                    <input type="text" class="form-control" id="keyword-choices" placeholder="<fmt:message
                            key="newBook.label.enter_keywords"/>">
                    <ul id="keyword-list">
                        <c:forEach items="${book.keywords}" var="keyword">
                            <li data-id="${keyword.id}">
                                    ${keyword.data}
                                <button class='btn btn-close btn-sm' aria-label='Close'></button>
                            </li>
                        </c:forEach>
                    </ul>
                    <input type="hidden" name="keywords" id="keywords-input">
                </div>
                <div class="form-group">
                    <button type="submit" id="form-submit" class="btn btn-primary">
                        <span><fmt:message key="newRequest.label.submit"/></span>
                    </button>
                </div>
                <div class="form-group">
                    <a class="btn btn-danger" href='/library/books/admin/page'>
                        <fmt:message key="newRequest.label.close"/>
                    </a>
                </div>
            </form>
            <script>
                $(document).ready(function () {
                    $('#add-book-form').submit(function (e) {
                        const authors = $('#author-list').children();
                        if (authors.length === 0) {
                            alert("<fmt:message key="newBook.alert.authors"/>");
                            e.preventDefault();
                        }
                        const keywords = $('#keyword-list').children();
                        if (keywords.length === 0) {
                            alert("<fmt:message key="newBook.alert.keywords"/>");
                            e.preventDefault();
                        }
                    });
                    document.getElementById("publicationDate").max = new Date().toISOString().split("T")[0];
                    $('#first-name-error').hide();
                    $('#middle-name-error').hide();
                    $('#last-name-error').hide();
                });

            </script>

        </div>

    </jsp:body>
</tag:authorization>