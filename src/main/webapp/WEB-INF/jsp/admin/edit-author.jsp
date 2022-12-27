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
        <title><fmt:message key="editAuthor.pageTitle"/></title>
    </jsp:attribute>
    <jsp:body>
        <div class="container main-content">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="/"><fmt:message key="home.pageTitle"/></a></li>
                <li class="breadcrumb-item"><a href="/library/admin/authors"><fmt:message key="authorList.pageTitle"/></a></li>
            </ol>
            <h1><fmt:message key="editAuthor.pageTitle"/></h1>

            <form class="main" method="post">
                <textarea name="firstName" class="form-control" placeholder="<fmt:message key="editAuthor.firstName"/>">${author.firstName}</textarea>
                <textarea name="lastName" class="form-control" placeholder="<fmt:message key="editAuthor.lastName"/>">${author.lastName}</textarea>
                <div class="form-group">
                    <input type="submit" class="btn btn-primary" value="<fmt:message key="editUser.label.submit" />"/>
                </div>
            </form>
        </div>
        <script type="text/javascript">
            $(function () {
                $('form').submit(function () {
                    $(':submit', this).attr('disabled', 'disabled');
                });
            })
        </script>
    </jsp:body>
</tag:authorization>