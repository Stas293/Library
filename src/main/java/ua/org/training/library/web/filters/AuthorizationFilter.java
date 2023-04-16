package ua.org.training.library.web.filters;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.constants.Values;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.security.AuthorityUser;
import ua.org.training.library.security.SecurityService;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AuthorizationFilter implements Filter {
    private final SecurityService securityService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        final HttpServletRequest req = (HttpServletRequest) servletRequest;
        final HttpServletResponse res = (HttpServletResponse) servletResponse;

        log.debug("AuthorizationFilter filter");
        String path = req.getRequestURI();

        log.debug("Path: " + path);
        AuthorityUser authorityUser = securityService.getAuthorityUser(req);

        log.debug("Authority user: " + authorityUser);
        boolean hasAccess = checkAccess(path, authorityUser);

        log.debug("Has access: " + hasAccess);
        if (!hasAccess) {
            log.debug(String.format("Access denied to %s", path));
            log.debug(String.format("User %s has no access to %s", authorityUser, path));
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            securityService.removeLoggedUserFromSession(req, Objects.requireNonNull(authorityUser).getLogin());
            req.setAttribute(Values.APP_MESSAGE_ATTRIBUTE,
                    Values.BUNDLE_ACCESS_DENIED_MESSAGE);
        } else filterChain.doFilter(req, res);
    }

    private boolean checkAccess(String path, AuthorityUser authorityUser) {
        return Arrays.stream(Values.APP_ROLES)
                .filter(role -> path.contains(role.toLowerCase()))
                .findFirst()
                .map(role -> authorityUser != null &&
                        authorityUser.hasRole(role))
                .orElse(true);
    }
}
