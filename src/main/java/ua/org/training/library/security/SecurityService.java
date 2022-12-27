package ua.org.training.library.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.dao.DaoFactory;
import ua.org.training.library.dao.SecurityDao;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.exceptions.JDBCException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.utility.Constants;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class SecurityService {
    private static final Logger LOGGER = LogManager.getLogger(SecurityService.class);

    public static AuthorityUser getAuthorityUser(HttpServletRequest request) {
        return Optional.ofNullable(
                        (AuthorityUser) request.getSession()
                                .getAttribute(Constants.RequestAttributes
                                        .USER_ATTRIBUTE))
                .orElse(AuthorityUser.ANONYMOUS);
    }

    public static boolean checkIfUserIsLogged(HttpServletRequest request, String login) {
        Set<String> loggedLogins = Optional.ofNullable(
                        (Set<String>) request.getServletContext()
                                .getAttribute(Constants
                                        .RequestAttributes
                                        .APP_LOGGED_USERS_HASH_SET_ATTRIBUTE))
                .orElse(new HashSet<>());
        if (loggedLogins.stream().anyMatch(login::equals)) {
            LOGGER.info("User with login {} is already logged in", login);
            return true;
        }
        return false;
    }

    public static void addLoggedUserToContext(HttpServletRequest request, AuthorityUser authorityUser) {
        Set<String> loggedUserLogins = Optional.ofNullable(
                        (Set<String>) request.getServletContext()
                                .getAttribute(Constants
                                        .RequestAttributes
                                        .APP_LOGGED_USERS_HASH_SET_ATTRIBUTE))
                .orElse(new HashSet<>());
        loggedUserLogins.add(authorityUser.getLogin());
        request.getSession().setAttribute(Constants.RequestAttributes.USER_ATTRIBUTE, authorityUser);
        request.getServletContext().setAttribute(Constants.RequestAttributes.APP_LOGGED_USERS_HASH_SET_ATTRIBUTE, loggedUserLogins);
    }

    public static void removeLoggedUserFromSession(HttpServletRequest request, String login) {
        Set<String> loggedUserLogins = Optional.ofNullable(
                        (Set<String>) request.getServletContext()
                                .getAttribute(Constants
                                        .RequestAttributes
                                        .APP_LOGGED_USERS_HASH_SET_ATTRIBUTE))
                .orElse(new HashSet<>());
        loggedUserLogins.remove(login);
        request.getSession().removeAttribute(Constants.RequestAttributes.USER_ATTRIBUTE);
        request.getServletContext().setAttribute(Constants.RequestAttributes.APP_LOGGED_USERS_HASH_SET_ATTRIBUTE, loggedUserLogins);
    }

    public static void removeLoggedUserFromContext(HttpSession session) throws ServiceException {
        Set<String> loggedUserLogins = Optional.ofNullable(
                (Set<String>)
                        session.getServletContext()
                                .getAttribute(Constants
                                        .RequestAttributes
                                        .APP_LOGGED_USERS_HASH_SET_ATTRIBUTE))
                .orElse(new HashSet<>());
        AuthorityUser authorityUser = (AuthorityUser) Optional
                .ofNullable(session.getAttribute(Constants.RequestAttributes.USER_ATTRIBUTE))
                .orElseThrow(() -> new ServiceException("User not found in session"));
        loggedUserLogins.remove(authorityUser.getLogin());
        session.removeAttribute(Constants.RequestAttributes.USER_ATTRIBUTE);
        session.getServletContext().setAttribute(Constants.RequestAttributes.APP_LOGGED_USERS_HASH_SET_ATTRIBUTE, loggedUserLogins);
    }

    public static String getCurrentLogin(HttpSession session) throws ServiceException {
        AuthorityUser authorityUser = (AuthorityUser) Optional
                .ofNullable(session.getAttribute(Constants.RequestAttributes.USER_ATTRIBUTE))
                .orElseThrow(() -> new ServiceException("User not found in session"));
        return authorityUser.getLogin();
    }

    public static String getUserPassword(String login) throws ServiceException {
        try (SecurityDao securityDao = DaoFactory.getInstance().createSecurityDao()) {
            return securityDao.getPasswordByLogin(login);
        } catch (JDBCException e) {
            LOGGER.error("Can't create SecurityDao", e);
        } catch (DaoException e) {
            LOGGER.error("Error while getting user password", e);
            throw new ServiceException("Error while getting user password", e);
        } catch (Exception e) {
            LOGGER.error("Error while getting user password", e);
            throw new ServiceException("Error while getting user password", e);
        }
        return null;
    }
}
