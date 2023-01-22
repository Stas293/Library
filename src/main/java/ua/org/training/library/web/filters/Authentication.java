package ua.org.training.library.web.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.security.AuthorityUser;
import ua.org.training.library.security.SecurityService;
import ua.org.training.library.utility.Constants;

import java.io.IOException;
import java.util.Arrays;

@WebFilter(filterName = "Authentication", urlPatterns = {"/*"})
public class Authentication implements Filter {
    private static final Logger LOGGER = LogManager.getLogger(Authentication.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        final HttpServletRequest req = (HttpServletRequest) servletRequest;
        final HttpServletResponse res = (HttpServletResponse) servletResponse;

        String path = req.getRequestURI();
        AuthorityUser authorityUser = SecurityService.getAuthorityUser(req);

        boolean hasAccess = Arrays.stream(Constants.APP_ROLES)
                .filter(role -> path.contains(role.toLowerCase()))
                .findFirst()
                .map(role -> authorityUser != null &&
                        authorityUser.hasRole(role))
                .orElse(true);
        if (!hasAccess) {
            LOGGER.debug(String.format("Access denied to %s", path));
            LOGGER.debug(String.format("User %s has no access to %s", authorityUser, path));
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            SecurityService.removeLoggedUserFromSession(req, authorityUser.getLogin());
            req.setAttribute(Constants.RequestAttributes.APP_MESSAGE_ATTRIBUTE,
                    Constants.BUNDLE_ACCESS_DENIED_MESSAGE);
        } else filterChain.doFilter(req, res);
    }
}
