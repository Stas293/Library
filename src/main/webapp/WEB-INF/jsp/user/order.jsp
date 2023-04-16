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
                    <a href="/library/user/page">
                        <fmt:message key="home.pageTitle"/>
                    </a>
                </li>
            </ol>
            <h1><fmt:message key="order.created.title"/></h1>
            <h2><span class="text-primary">${order.book.title}</span></h2>
            <h3>${book.description}</h3>
            <h3><fmt:message key="newRequest.label.isbn"/> ${order.book.isbn}</h3>
            <h3><fmt:message key="newRequest.label.publicationDate"/> ${order.book.publicationDate}</h3>
            <h3><fmt:message key="newRequest.label.fine"/> ${order.book.fine}</h3>
            <h3>${order.book.language}</h3>
            <h3><fmt:message key="newRequest.label.place"/> ${order.place.name}</h3>
            <h3><fmt:message key="newRequest.label.status"/> ${order.status.value}</h3>
        </div>

    </jsp:body>
</tag:authorization>