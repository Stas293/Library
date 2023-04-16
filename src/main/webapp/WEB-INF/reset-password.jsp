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
        <title><fmt:message key="personal.edit.data"/></title>
    </jsp:attribute>
    <jsp:body>
        <div class="container main-content">
            <ol class="breadcrumb list-group-item-dark rounded">
                <li class="breadcrumb-item"><a href="/"><fmt:message key="home.pageTitle"/></a></li>
                <li class="breadcrumb-item active"><a href="/library/login"><fmt:message key="subhead.welcome"/></a></li>
            </ol>
            <h1 class="text-center mb-4"><fmt:message key="user.reset.password"/></h1>
            <form class="form-floating" method="post">
                <p><fmt:message key="user.reset.all.fields"/></p>
                <div class="mb-3">
                    <label for="email">
                        <fmt:message key="user.input.email"/>
                    </label>
                    <input required name="email" type="email" class="form-control" id="email">
                    <c:set var="emailError">${error}</c:set>
                    <c:if test="${not empty emailError}">
                        <div class="alert alert-danger"><fmt:message key="user.email.not.exists"/></div>
                    </c:if>
                </div>
                <button type="submit" class="btn btn-primary">
                    <fmt:message key="user.send.reset"/>
                </button>
            </form>
        </div>
        <script type="text/javascript">
            $(document).ready(function () {
                $('form').submit(function () {
                    $(':submit', this).attr('disabled', 'disabled');
                });
            });
        </script>
    </jsp:body>
</tag:authorization>