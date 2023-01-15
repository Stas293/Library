<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<fmt:requestEncoding value="UTF-8"/>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="interface"/>


<t:authorization>
    <jsp:attribute name="head">
        <link rel="stylesheet" href="css/main-page-style.css">
        <title>
            Library
        </title>
    </jsp:attribute>
    <jsp:body>
        <div class="container main-content">
            <div id="carouselExampleControls" class="carousel box-slider">
                <div class="carousel-inner" role="listbox">
                    <div id="home" class="first-section">
                        <div class="dtab">
                            <div class="container">
                                <div class="col-md-12 col-sm-12 text-right">
                                    <div class="big-tagline">
                                        <h2><strong><fmt:message key="app.title"/></strong> <fmt:message
                                                key="app.page.header.company"/></h2>
                                        <p class="lead"><fmt:message key="app.slogan"/></p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </jsp:body>
</t:authorization>
