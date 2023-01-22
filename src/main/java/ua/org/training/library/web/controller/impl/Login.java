package ua.org.training.library.web.controller.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;
import ua.org.training.library.context.ApplicationContext;
import ua.org.training.library.exceptions.ConnectionDBException;
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
    private final UserService userService = ApplicationContext.getInstance().getUserService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String login = Utility.getStringParameter(request.getParameter(Constants.RequestAttributes.APP_LOGIN_ATTRIBUTE),
                Constants.APP_STRING_DEFAULT_VALUE);
        String password = Utility.getStringParameter(request.getParameter(Constants.RequestAttributes.APP_PASSWORD_ATTRIBUTE),
                Constants.APP_STRING_DEFAULT_VALUE);

        AuthorityUser authorityUser = SecurityService.getAuthorityUser(request);

        if (!authorityUser.getLogin().equals(Constants.APP_UNAUTHORIZED_USER)) {
            SecurityService.removeLoggedUserFromSession(request, authorityUser.getLogin());
            return Links.LOGIN_PAGE;
        }

        if (login.equals(Constants.APP_STRING_DEFAULT_VALUE) || password.equals(Constants.APP_STRING_DEFAULT_VALUE)) {
            return Links.LOGIN_PAGE;
        }

        if (SecurityService.checkIfUserIsLogged(request, login)) {
            return sendForbidden(request, response);
        }

        try {
            authorityUser = new AuthorityUser(userService.getUserByLogin(login));
        } catch (ServiceException e) {
            LOGGER.error(e.getMessage(), e);
            authorityUser = AuthorityUser.ANONYMOUS;
        } catch (ConnectionDBException e) {
            LOGGER.error(e.getMessage(), e);
            return Links.ERROR_PAGE + "?message=" + e.getMessage();
        }

        if (!authorityUser.isEnabled()) {
            request.setAttribute(Constants.RequestAttributes.APP_DISABLED, Constants.APP_DISABLED_USER);
            return Links.LOGIN_PAGE;
        }

        LOGGER.info(String.format("User with full name %s and roles %s is trying to login", authorityUser.getFullName(), authorityUser.getRoles()));
        String adminPageRedirect = checkPasswordAndLogIn(request, password, authorityUser);
        if (adminPageRedirect != null) return adminPageRedirect;

        request.setAttribute(Constants.RequestAttributes.APP_ERROR_ATTRIBUTE, "true");
        return Links.LOGIN_PAGE;
    }

    private static String checkPasswordAndLogIn(HttpServletRequest request, String password, AuthorityUser authorityUser) {
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
        } catch (ConnectionDBException e) {
            LOGGER.error(e.getMessage(), e);
            return Links.ERROR_PAGE + "?message=" + e.getMessage();
        }
        return null;
    }

    private static String sendForbidden(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            request.setAttribute(Constants.RequestAttributes.APP_MESSAGE_ATTRIBUTE, Constants.BUNDLE_ACCESS_DENIED_LOGGED_USERS);
            return Links.LOGIN_PAGE;
        } catch (IOException e) {
            LOGGER.error("Error user is already logged", e);
        }
        return Constants.APP_STRING_DEFAULT_VALUE;
    }
}
