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
        <div id="author_delete_success" hidden="hidden"><fmt:message key="admin.delete.author.success"/></div>
        <div id="author_delete_error" hidden="hidden"><fmt:message key="admin.delete.author.error"/></div>
        <div id="edit_author_button" hidden="hidden"><fmt:message key="admin.edit.author.button"/></div>
        <div id="delete_author_button" hidden="hidden"><fmt:message key="admin.delete.author.button"/></div>
        <div class="container main-content">
            <ol class="breadcrumb list-group-item-dark rounded">
                <li class="breadcrumb-item active"><a href="/"><fmt:message key="home.pageTitle"/></a></li>
            </ol>
            <c:set var="firstNameErrors">${authorValidationError.firstName}</c:set>
            <c:if test="${not empty firstNameErrors}">
                <div class="alert alert-danger"><fmt:message key="${authorValidationError.firstName}"/></div>
            </c:if>
            <c:set var="firstNameErrors">${authorValidationError.lastName}</c:set>
            <c:if test="${not empty firstNameErrors}">
                <div class="alert alert-danger"><fmt:message key="${authorValidationError.lastName}"/></div>
            </c:if>
            <h1>
                <fmt:message key="authorList.pageTitle"/>
            </h1>
            <div class="modal fade" id="create-author-modal" tabindex="-1" aria-labelledby="create-author-modal-label"
                 aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="create-author-modal-label">Create Author</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <form id="create-author-form" method="POST" action="/authors">
                                <div class="mb-3">
                                    <label for="first-name" class="form-label"><fmt:message
                                            key="author.firstName"/></label>
                                    <input type="text" class="form-control" id="first-name" name="firstName" required>
                                    <div class="alert alert-danger" id="first-name-error">
                                        <fmt:message key="form.validation.name"/>
                                    </div>
                                </div>
                                <div class="mb-3">
                                    <label for="middle-name" class="form-label"><fmt:message
                                            key="author.middleName"/></label>
                                    <input type="text" class="form-control" id="middle-name" name="middleName">
                                    <div class="alert alert-danger" id="middle-name-error">
                                        <fmt:message key="form.validation.name"/>
                                    </div>
                                </div>
                                <div class="mb-3">
                                    <label for="last-name" class="form-label"><fmt:message
                                            key="author.lastName"/></label>
                                    <input type="text" class="form-control" id="last-name" name="lastName" required>
                                    <div class="alert alert-danger" id="last-name-error">
                                        <fmt:message key="form.validation.name"/>
                                    </div>
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            <button type="submit" form="create-author-form" class="btn btn-primary">Create</button>
                        </div>
                    </div>
                </div>
            </div>
            <div class="grid-container">
                <div class="grid-left-3">
                    <div class="list-group list-group-flush components">
                        <label class="library-create-book btn-lg" id="new-book-window" data-bs-toggle="modal"
                               data-bs-target="#create-author-modal">
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
                            <th id="author_id" scope="col"><fmt:message key="author.table.author_id"/></th>
                            <th id="author_first_name" scope="col"><fmt:message
                                    key="author.table.first_name"/></th>
                            <th id="author_middle_name" scope="col"><fmt:message
                                    key="author.table.middle_name"/></th>
                            <th id="author_last_name" scope="col"><fmt:message
                                    key="author.table.last_name"/></th>
                        </tr>
                        </thead>
                        <tbody id="pageable-list">
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <script>
            $(document).ready(function () {
                $('#first-name-error').hide();
                $('#middle-name-error').hide();
                $('#last-name-error').hide();
                $('#create-author-modal form').submit(function (event) {
                    // Prevent the form from submitting normally
                    event.preventDefault();

                    // Get the form data
                    const firstName = $('#first-name').val().trim();
                    let middleName = $('#middle-name').val().trim();
                    const lastName = $('#last-name').val().trim();

                    let dataToSend = {
                        firstName: firstName,
                        middleName: middleName,
                        lastName: lastName,
                    };

                    // Send an AJAX request to create the author
                    $.ajax({
                        url: '/library/authors/admin',
                        type: 'POST',
                        data: dataToSend,
                        success: function (data) {
                            // Display a success message
                            alert("<fmt:message key="admin.create.author.success"/>");

                            // Close the modal
                            $('#create-author-modal').modal('hide');

                            // Clear the form
                            $('#create-author-modal form')[0].reset();

                            wizard(urlPath);
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            const response = JSON.parse(jqXHR.responseText);

                            console.log(response);

                            // add error messages to the modal
                            if (response.containsErrors) {
                                // Show error messages for each field
                                if (response.firstName) {
                                    $('#first-name-error').show();
                                } else {
                                    $('#first-name-error').hide();
                                }

                                if (response.middleName) {
                                    $('#middle-name-error').show();
                                } else {
                                    $('#middle-name-error').hide();
                                }

                                if (response.lastName) {
                                    $('#last-name-error').show();
                                } else {
                                    $('#last-name-error').hide();
                                }
                            } else {
                                alert("<fmt:message key="admin.create.author.error"/>");
                            }
                        }
                    });
                });
            });
        </script>
    </jsp:body>
</tag:authorization>