package ua.org.training.library.web.controller.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.security.AuthorityUser;
import ua.org.training.library.security.SecurityService;
import ua.org.training.library.service.UserService;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.Links;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.web.controller.ControllerCommand;

import java.io.IOException;

public class Login implements ControllerCommand {
    private static final Logger LOGGER = LogManager.getLogger(Login.class);
    private final UserService userService = new UserService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String login = Utility.getStringParameter(request.getParameter(Constants.RequestAttributes.APP_LOGIN_ATTRIBUTE),
                Constants.APP_STRING_DEFAULT_VALUE);
        String password = Utility.getStringParameter(request.getParameter(Constants.RequestAttributes.APP_PASSWORD_ATTRIBUTE),
                Constants.APP_STRING_DEFAULT_VALUE);
        request.getSession().removeAttribute(Constants.RequestAttributes.APP_ERROR_ATTRIBUTE);

        AuthorityUser authorityUser = SecurityService.getAuthorityUser(request);

        if (!authorityUser.getLogin().equals(Constants.APP_UNAUTHORIZED_USER)) {
            SecurityService.removeLoggedUserFromSession(request, authorityUser.getLogin());
            return Links.LOGIN_PAGE;
        }

        if (login.equals(Constants.APP_STRING_DEFAULT_VALUE) || password.equals(Constants.APP_STRING_DEFAULT_VALUE)) {
            return Links.LOGIN_PAGE;
        }

        if (SecurityService.checkIfUserIsLogged(request, login)) {
            try {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                request.setAttribute(Constants.RequestAttributes.APP_MESSAGE_ATTRIBUTE, Constants.BUNDLE_ACCESS_DENIED_LOGGED_USERS);
                return Links.LOGIN_PAGE;
            } catch (IOException e) {
                LOGGER.error("Error user is already logged", e);
            }
            return Constants.APP_STRING_DEFAULT_VALUE;
        }

        try {
            authorityUser = new AuthorityUser(userService.getUserByLogin(login));
        } catch (ServiceException e) {
            LOGGER.error(e.getMessage(), e);
            authorityUser = AuthorityUser.ANONYMOUS;
        }

        if (!authorityUser.isEnabled()) {
            request.setAttribute(Constants.RequestAttributes.APP_DISABLED, Constants.APP_DISABLED_USER);
            return Links.LOGIN_PAGE;
        }

        LOGGER.info(String.format("User with full name %s and roles %s is trying to login", authorityUser.getFullName(), authorityUser.getRoles()));
        try {
            if (!authorityUser.getLogin().equals(Constants.APP_UNAUTHORIZED_USER) &&
                    BCrypt.checkpw(password, SecurityService.getUserPassword(authorityUser.getLogin()))) {

                SecurityService.addLoggedUserToContext(request, authorityUser);

                if (authorityUser.hasRole("ADMIN")) return Links.ADMIN_PAGE_REDIRECT;
                if (authorityUser.hasRole("LIBRARIAN")) return Links.LIBRARIAN_PAGE_REDIRECT;
                return Links.USER_PAGE_REDIRECT;
            }
        } catch (ServiceException e) {
            LOGGER.error(e.getMessage(), e);
        }
        clearRequestSessionAttributes(request);
        request.getSession().setAttribute(Constants.RequestAttributes.APP_ERROR_ATTRIBUTE, "true");
        return Links.LOGIN_PAGE;
    }

    @Override
    public void clearRequestSessionAttributes(HttpServletRequest request) {
        request.getSession().removeAttribute(Constants.RequestAttributes.APP_LOGIN_ATTRIBUTE);
        request.getSession().removeAttribute(Constants.RequestAttributes.APP_PASSWORD_ATTRIBUTE);
    }
}
