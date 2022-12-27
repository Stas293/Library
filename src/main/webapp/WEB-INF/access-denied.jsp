<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>

<fmt:requestEncoding value="UTF-8" />
<fmt:setLocale value="${lang}" />
<fmt:setBundle basename="interface"/>

<tag:authorization>
    <jsp:attribute name="head">
        <title><fmt:message key="access.denied.title"/></title>
    </jsp:attribute>
    <jsp:body>
        <div class="access-denied">
            <div class="error-message">
                <h1><fmt:message key="access.denied.title" /></h1>
                <fmt:message key="${message}" />
            </div>
        </div>
    </jsp:body>
</tag:authorization>
