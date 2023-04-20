<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>

<fmt:requestEncoding value="UTF-8"/>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="interface"/>

<tag:authorization>
    <jsp:attribute name="head">
    	<title><fmt:message key="admin.title"/></title>
    </jsp:attribute>
    <jsp:body>
        <div class="container mt-5 main-content">
        <h1 class="text-center mb-4">${book.title}</h1>
        <div class="row">
            <div class="col-md-8">
                <h2 class="mb-4">${book.authors}</h2>
                <div class="mb-4">
                    <fmt:message key="book.publicationDate"/>:
                        ${book.publicationDate}
                </div>
                <div class="mb-4">
                    <fmt:message key="book.description"/>:
                        ${book.description}
                </div>
                <div class="mb-4">
                    <fmt:message key="book.isbn"/>:
                        ${book.isbn}
                </div>
                <div class="mb-4">
                    <fmt:message key="book.language"/>:
                        ${book.language}
                </div>
                <div class="mb-4">
                    <fmt:message key="book.fine"/>:
                        ${book.fine}
                </div>
                <div class="mb-4">
                    <fmt:message key="book.location"/>:
                        ${book.location}
                    <div class="mb-4">
                        <fmt:message key="book.count"/>:
                            ${book.count}
                    </div>
                    <div class="mb-4">
                        <fmt:message key="book.keywords"/>:
                            ${book.keywords}
                    </div>
                    <a href="${pageContext.request.contextPath}/library/books/admin/${book.id}/edit" class="btn btn-primary">
                        <fmt:message key="admin.edit"/>
                    </a>
                    <form action="${pageContext.request.contextPath}/library/books/admin/${book.id}" method="post">
                        <input type="hidden" name="_method" value="DELETE"/>
                        <input type="submit" class="btn btn-danger" value="<fmt:message key="admin.delete"/>"/>
                    </form>
                </div>
            </div>
        </div>
    </jsp:body>
</tag:authorization>

