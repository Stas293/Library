<%@ tag isELIgnored="false" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="authorization-role" uri="/WEB-INF/tld/authorization.tld" %>

<fmt:requestEncoding value="UTF-8"/>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="interface"/>

<%@attribute name="head" fragment="true" %>

<html>
<head>
    <jsp:invoke fragment="head"/>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.1/jquery.min.js"></script>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/error.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Serif+Pro:400,600,700">
</head>
<body>
<header>
    <nav class="navbar navbar-expand-md navbar-dark bg-dark fixed-top">
        <div class="container-fluid">
            <a class="navbar-brand" href="/" title="<fmt:message key="app.title"/>">
                <fmt:message key="app.title"/>
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarCollapse">
                <i class="navbar-toggler-icon"></i>
            </button>
            <div class="navbar-collapse collapse" id="navbarCollapse">
                <div class="navbar-nav">
                    <a class="nav-item nav-link" href="/">
                        <fmt:message key="app.home"/>
                    </a>
                    <a class="nav-item nav-link" href="/library/books">
                        <fmt:message key="app.books"/>
                    </a>
                    <authorization-role:authority role="isNonAuthorized()">
                        <a class="nav-item nav-link" href="/library/register">
                            <fmt:message key="app.registration"/>
                        </a>
                    </authorization-role:authority>
                    <authorization-role:authority role="hasRole('ADMIN')">
                        <div class="navbar-nav dropdown">
                            <a class="nav-link dropdown-toggle" href="#" id="adminDropdown" role="button" data-bs-toggle="dropdown"
                                aria-expanded="false">
                                <fmt:message key="app.pageTitle.admin"/>
                            </a>
                            <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                                <li class="nav-item nav-link">
                                    <a class="dropdown-item" href="/library/admin/page">
                                        <fmt:message key="app.pageTitle.manage-users"/>
                                    </a>
                                </li>
                                <li class="nav-item nav-link">
                                    <a class="dropdown-item" href="/library/admin/books">
                                        <fmt:message key="app.pageTitle.manage-books"/>
                                    </a>
                                </li>
                                <li class="nav-item nav-link">
                                    <a class="dropdown-item" href="/library/admin/authors">
                                        <fmt:message key="app.pageTitle.manage-authors"/>
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </authorization-role:authority>
                    <authorization-role:authority role="hasRole('LIBRARIAN')">
                        <a class="nav-item nav-link" href="/library/order/librarian/page">
                            <fmt:message key="app.pageTitle.librarian"/>
                        </a>
                    </authorization-role:authority>
                    <authorization-role:authority role="hasRole('USER')">
                        <a class="nav-item nav-link" href="/library/order/user/page">
                            <fmt:message key="app.pageTitle.user"/>
                        </a>
                        <a class="nav-item nav-link" href="/library/user/history/page">
                            <fmt:message key="app.pageTitle.history"/>
                        </a>
                    </authorization-role:authority>
                    <authorization-role:authority role="isAuthorized()">
                        <a class="nav-item nav-link" href="/library/user/personal-data">
                            <fmt:message key="app.pagePersonal"/>
                        </a>
                    </authorization-role:authority>
                </div>
            </div>

            <div class="navbar-nav dropdown">
                <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown"
                   aria-expanded="false">
                    <fmt:message key="app.lang"/>
                </a>
                <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                    <li class="nav-item nav-link">
                        <a class="dropdown-item" href="?lang=en">
                            <fmt:message key="app.lang.english"/>
                        </a>
                    </li>
                    <li class="nav-item nav-link">
                        <a class="dropdown-item" href="?lang=uk">
                            <fmt:message key="app.lang.ukrainian"/>
                        </a>
                    </li>
                </ul>
            </div>

            <ul class="navbar-nav">
                <li class="nav-item nav-link">
                    <authorization-role:authority role="isNonAuthorized()">
                        <div class="library-sessionInfo">
                            <fmt:message key="subhead.welcome"/>
                            <a class="library-login-logout-btn" href="/library/login">
                                <fmt:message key="subhead.login"/>
                            </a>
                        </div>
                    </authorization-role:authority>
                </li>
                <li class="nav-item">
                    <authorization-role:authority role="isAuthorized()">
                        <div class="library-sessionInfo">
                                ${user.fullName}
                            <a class="library-login-logout-btn" href="/library/logout">
                                <fmt:message key="subhead.logout"/>
                            </a>
                        </div>
                    </authorization-role:authority>
                </li>
            </ul>
        </div>
    </nav>
</header>
<div class="content">
    <jsp:doBody/>
</div>
<footer class="footer">
    <p class="text-center text-uppercase text-muted">&copy;<fmt:message key="app.footer"/></p>
</footer>
</body>
</html>