package ua.org.training.library.web.filters;


import jakarta.annotation.PostConstruct;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.enums.DefaultValues;
import ua.org.training.library.security.AuthorityUser;
import ua.org.training.library.security.SecurityService;
import ua.org.training.library.service.RoleService;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthorizationFilter implements Filter {
    private final SecurityService securityService;
    private final RoleService roleService;
    private String[] CODE_ROLES;

    @PostConstruct
    public void init() {
        CODE_ROLES = roleService.findAllCode();
    }

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
            req.setAttribute(DefaultValues.APP_MESSAGE_ATTRIBUTE.getValue(),
                    DefaultValues.BUNDLE_ACCESS_DENIED_MESSAGE.getValue());
        } else filterChain.doFilter(req, res);
    }

    private boolean checkAccess(String path, AuthorityUser authorityUser) {
        return Arrays.stream(CODE_ROLES)
                .filter(role -> path.contains(role.toLowerCase()))
                .findFirst()
                .map(role -> authorityUser != null &&
                        authorityUser.hasRole(role))
                .orElse(true);
    }
}
