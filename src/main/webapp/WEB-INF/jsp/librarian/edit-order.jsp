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
                <ol class="breadcrumb bg-dark rounded mb-3">
                    <li class="breadcrumb-item"><a href="/" class="text-light"><fmt:message key="home.pageTitle"/></a>
                    </li>
                    <li class="breadcrumb-item active"><a href="/library/order/librarian/page"
                                                          class="text-light"><fmt:message
                            key="librarian.changeOrderStatus.pageTitle"/></a></li>
                </ol>
            </nav>

            <h1 class="mb-3"><fmt:message key="orderBook.pageTitle"/></h1>

            <form id="add-book-form" data-toggle="validator" action="/library/order/librarian/edit-order" method="post">
                <input type="hidden" name="_method" value="PATCH"/>
                <input type="hidden" name="orderId" id="orderId" value="${order.id}"/>

                <div class="mb-3">
                    <h2 class="text-primary">${order.book.title}</h2>
                    <h3>${order.book.description}</h3>
                    <h3><fmt:message key="orderBook.label.isbn"/> ${order.book.isbn}</h3>
                    <h3><fmt:message key="orderBook.label.publicationDate"/> ${order.book.publicationDate}</h3>
                    <h3><fmt:message key="orderBook.label.place"/> ${order.place.name}</h3>
                    <h3><fmt:message key="newRequest.label.date"/> ${order.dateCreated}</h3>
                    <h3><fmt:message key="newRequest.label.fine"/> ${order.book.fine}</h3></h3>
                    <c:if test="${order.dateExpire != null}">
                        <h3><fmt:message key="newRequest.label.dateExpire"/> ${order.dateExpire}</h3>
                    </c:if>
                    <h3><fmt:message key="newRequest.label.user"/> ${order.user.login}</h3>
                </div>

                <div class="form-group mb-3">
                    <label for="status" class="form-label"><fmt:message key="newRequest.label.status"/></label>
                    <select class="form-select" id="status" name="status" required>
                        <c:forEach items="${order.status.nextStatuses}" var="status">
                            <option value="${status.key}" closed="${status.value.closed}">
                                    ${status.value.value}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <c:if test="${order.place.choosable == true}">
                    <div class="form-group mb-3"
                         id="chooseDateExpiration">
                        <label for="dateExpire" class="form-label"><fmt:message
                                key="newRequest.label.dateExpire"/></label>
                        <input type="date" class="form-control" id="dateExpire" name="dateExpire"
                               value="${order.place.defaultDate}" required/>
                    </div>
                </c:if>

                <c:if test="${order.place.choosable == false}">
                    <input type="date" class="form-control" id="dateExpire" name="dateExpire"
                           value="${order.place.defaultDate}" hidden/>
                </c:if>


                <div class="d-grid gap-2 mb-3">
                    <button type="submit" id="form-submit" class="btn btn-primary" name="submit"><fmt:message
                            key="newRequest.label.submit"/></button>
                    <a class="btn btn-danger" href='/library/order/librarian/page'><fmt:message
                            key="newRequest.label.cancel"/></a>
                </div>

            </form>

        </div>

        <script>
            $(document).ready(function () {
                $('#dateExpire').attr('min', new Date().toISOString().split('T')[0]);
                if ($('#status option:selected').attr('closed') === 'true') {
                    $('#chooseDateExpiration').hide();
                }

                $('#status').change(function () {
                    if ($('#status option:selected').attr('closed') === 'true') {
                        $('#chooseDateExpiration').hide();
                    } else {
                        $('#chooseDateExpiration').show();
                    }
                });
            });
        </script>

    </jsp:body>
</tag:authorization>