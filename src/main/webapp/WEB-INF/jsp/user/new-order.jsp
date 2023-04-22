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
    </jsp:attribute>
    <jsp:body>
        <div class="container main-content">
            <nav aria-label="breadcrumb">
                <ol class="breadcrumb bg-dark rounded">
                    <li class="breadcrumb-item"><a href="/" class="text-white"><fmt:message key="home.pageTitle"/></a>
                    </li>
                    <li class="breadcrumb-item active">
                        <a href="/library/order/user/page" class="text-white"><fmt:message key="orderList.pageTitle"/></a>
                </ol>
            </nav>
            <h1 class="my-4"><fmt:message key="orderBook.pageTitle"/></h1>
            <div class="row">
                <div class="col-lg-8">
                    <div class="card mb-4">
                        <div class="card-body">
                            <h2 class="card-title"><span class="text-primary">${book.title}</span></h2>
                            <h3>${book.description}</h3>
                            <ul class="list-unstyled">
                                <li><strong><fmt:message key="newRequest.label.isbn"/>:</strong> ${book.isbn}</li>
                                <li><strong><fmt:message key="newRequest.label.count"/>:</strong> ${book.count}</li>
                                <li><strong><fmt:message
                                        key="newRequest.label.publicationDate"/>:</strong> ${book.publicationDate}</li>
                                <li><strong><fmt:message key="newRequest.label.fine"/>:</strong> ${book.fine}</li>
                                <li><strong><fmt:message key="newRequest.label.authors"/>:</strong> ${book.authors}</li>
                                <li><strong><fmt:message key="newRequest.label.keywords"/>:</strong> ${book.keywords}
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="col-lg-4">
                    <div class="card mb-4">
                        <div class="card-body">
                            <h3 class="card-title"><fmt:message key="newRequest.label.place"/></h3>
                            <form id="add-book-form" data-toggle="validator" action="/library/order/user/${book.id}"
                                  method="post">
                                <input type="hidden" name="bookId" value="${book.id}"/>
                                <div class="form-group mb-3">
                                    <select required name="place" class="form-select">
                                        <c:forEach items="${places}" var="place">
                                            <option value="${place.id}">${place.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="d-grid gap-2">
                                    <button type="submit" id="form-submit" class="btn btn-primary" name="submit">
                                        <fmt:message key="newRequest.label.submit"/>
                                    </button>
                                    <a class="btn btn-danger" href='/library/order/user/page'>
                                        <fmt:message key="newRequest.label.cancel"/>
                                    </a>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>


    </jsp:body>
</tag:authorization>