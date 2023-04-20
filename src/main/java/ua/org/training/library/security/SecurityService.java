package ua.org.training.library.security;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.enums.DefaultValues;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.exceptions.ServiceException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
public class SecurityService {

    public AuthorityUser getAuthorityUser(HttpServletRequest request) {
        return Optional.ofNullable(
                        (AuthorityUser) request.getSession()
                                .getAttribute(DefaultValues.USER_ATTRIBUTE.getValue()))
                .orElse(AuthorityUser.ANONYMOUS);
    }

    @SuppressWarnings("unchecked")
    public boolean checkIfUserIsLogged(HttpServletRequest request, String login) {
        Set<String> loggedLogins = Optional.ofNullable(
                        (Set<String>) request.getServletContext()
                                .getAttribute(DefaultValues.LOGGED_USERS_SET_CONTEXT.getValue()))
                .orElse(new HashSet<>());
        if (loggedLogins.stream().anyMatch(login::equals)) {
            log.info("User with login {} is already logged in", login);
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public void addLoggedUserToContext(HttpServletRequest request, AuthorityUser authorityUser) {
        Set<String> loggedUserLogins = Optional.ofNullable(
                        (Set<String>) request.getServletContext()
                                .getAttribute(DefaultValues.LOGGED_USERS_SET_CONTEXT.getValue()))
                .orElse(new HashSet<>());
        loggedUserLogins.add(authorityUser.getLogin());
        request.getSession().setAttribute(DefaultValues.USER_ATTRIBUTE.getValue(), authorityUser);
        request.getServletContext().setAttribute(DefaultValues.LOGGED_USERS_SET_CONTEXT.getValue(), loggedUserLogins);
    }

    @SuppressWarnings("unchecked")
    public void removeLoggedUserFromSession(HttpServletRequest request, String login) {
        Set<String> loggedUserLogins = Optional.ofNullable(
                        (Set<String>) request.getServletContext()
                                .getAttribute(DefaultValues.LOGGED_USERS_SET_CONTEXT.getValue()))
                .orElse(new HashSet<>());
        loggedUserLogins.remove(login);
        request.getSession().removeAttribute(DefaultValues.USER_ATTRIBUTE.getValue());
        request.getServletContext().setAttribute(DefaultValues.LOGGED_USERS_SET_CONTEXT.getValue(), loggedUserLogins);
    }

    @SuppressWarnings("unchecked")
    public void removeLoggedUserFromContextAndSession(HttpSession session) throws ServiceException {
        Set<String> loggedUserLogins = Optional.ofNullable(
                        (Set<String>)
                                session.getServletContext()
                                        .getAttribute(DefaultValues.LOGGED_USERS_SET_CONTEXT.getValue()))
                .orElse(new HashSet<>());
        AuthorityUser authorityUser = (AuthorityUser) Optional
                .ofNullable(session.getAttribute(DefaultValues.USER_ATTRIBUTE.getValue()))
                .orElseThrow(() -> new ServiceException("User not found in session"));
        loggedUserLogins.remove(authorityUser.getLogin());
        session.removeAttribute(DefaultValues.USER_ATTRIBUTE.getValue());
        session.getServletContext().setAttribute(DefaultValues.LOGGED_USERS_SET_CONTEXT.getValue(), loggedUserLogins);
    }

    public String getCurrentLogin(HttpSession session) throws ServiceException {
        AuthorityUser authorityUser = (AuthorityUser) Optional
                .ofNullable(session.getAttribute(DefaultValues.USER_ATTRIBUTE.getValue()))
                .orElseThrow(() -> new ServiceException("User not found in session"));
        return authorityUser.getLogin();
    }

    public boolean checkIfCurrentUserIsLogged(HttpServletRequest request) {
        return Optional.ofNullable(
                        (AuthorityUser) request.getSession()
                                .getAttribute(DefaultValues.USER_ATTRIBUTE.getValue()))
                .isPresent();
    }
}
