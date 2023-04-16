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
    	<title><fmt:message key="login.in.please"/></title>
    </jsp:attribute>
  <jsp:body>
    <div class="container mt-5 main-content">
      <h1 class="text-center mb-4"><fmt:message key="login.in.please"/></h1>
      <form action="/library/login" method="post">
        <c:if test="${requestScope.error}">
          <div class="alert alert-danger"><fmt:message key="login.warning.alert"/></div>
        </c:if>
        <div class="mb-3">
          <label for="login" class="form-label"><fmt:message key="login.login"/></label>
          <input type="text" class="form-control" id="login" name="login" aria-describedby="loginHelp"
                 placeholder="<fmt:message key="login.login.input" />">
          <small id="loginHelp" class="form-text text-muted text-sm-start"><fmt:message
                  key="login.never.share"/></small>
        </div>
        <div class="mb-3">
          <label for="password" class="form-label"><fmt:message key="login.password"/></label>
          <input type="password" class="form-control" id="password" name="password"
                 placeholder="<fmt:message key="login.password.input" />">
        </div>
        <button type="submit" class="ui-button btn btn-primary"><fmt:message key="login.in"/></button>
      </form>

      <div class="mt-3">
        <a href="library/forgot-password"><fmt:message key="login.forgot.password"/></a>
      </div>
    </div>
  </jsp:body>
</tag:authorization>

