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
            <ol class="breadcrumb list-group-item-dark rounded">
                <li class="breadcrumb-item"><a href="/"><fmt:message key="home.pageTitle"/></a></li>
                <li class="breadcrumb-item active">
                    <a href="/library/order/user/page">
                        <fmt:message key="orderList.pageTitle"/>
                    </a>
                </li>
            </ol>
            <h1><fmt:message key="orderBook.pageTitle"/></h1>
            <form id="add-book-form" data-toggle="validator" action="/library/order/user/${book.id}"
                  method="post">
                <input type="hidden" name="bookId" value="${book.id}"/>
                <h2><span class="text-primary">${book.title}</span> </h2>
                <h3>${book.description}</h3>
                <h3><fmt:message key="newRequest.label.isbn"/> ${book.isbn}</h3>
                <h3><fmt:message key="newRequest.label.count"/> ${book.count}</h3>
                <h3><fmt:message key="newRequest.label.publicationDate"/> ${book.publicationDate}</h3>
                <h3><fmt:message key="newRequest.label.fine"/> ${book.fine}</h3>
                <h3>${book.language}</h3>
                <h3>${book.location}</h3>
                <h3><fmt:message key="newRequest.label.authors"/> ${book.authors}</h3>
                <h3><fmt:message key="newRequest.label.keywords"/> ${book.keywords}</h3>

                <div class="form-group">
                    <label for="place" class="col-form-label-lg">
                        <fmt:message key="newRequest.label.place"/>
                    </label>
                    <select required name="place" class="form-control" id="place">
                        <c:forEach items="${places}" var="places">
                            <option value="${places.id}">${places.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <button type="submit" id="form-submit" class="btn btn-primary" name="submit">
                        <fmt:message key="newRequest.label.submit"/>
                    </button>
                </div>
                <div class="form-group">
                    <a class="btn btn-danger" href='/library/order/user/page'>
                        <fmt:message key="newRequest.label.cancel"/>
                    </a>
                </div>
            </form>
        </div>

    </jsp:body>
</tag:authorization>