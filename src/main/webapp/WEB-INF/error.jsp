<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>

<fmt:requestEncoding value="UTF-8" />
<fmt:setLocale value="${lang}" />
<fmt:setBundle basename="interface"/>

<tag:authorization>
    <jsp:attribute name="head">
        <title><fmt:message key="error.title"/></title>
    </jsp:attribute>
    <jsp:body>
        <div class="error-body">
            <div class="error-message">
                <h1><fmt:message key="error.title" /></h1>
                <c:if test="${param.failedconnection == true}"><p><fmt:message key="error.dbconnection" /></p></c:if>
                <p><fmt:message key="error.contact.admin" /></p>
            </div>
        </div>
    </jsp:body>
</tag:authorization>
