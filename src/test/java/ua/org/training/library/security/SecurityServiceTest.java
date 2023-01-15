package ua.org.training.library.security;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.model.Role;
import ua.org.training.library.model.User;
import ua.org.training.library.utility.Constants;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SecurityServiceTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession httpSession;
    @Mock
    private ServletContext servletContext;

    @Test
    void getAuthorityUser() {
        Mockito.when(request.getSession()).thenReturn(httpSession);
        Mockito.when(httpSession.getAttribute(Constants.RequestAttributes
                .USER_ATTRIBUTE)).thenReturn(null);
        AuthorityUser authorityUser = SecurityService.getAuthorityUser(request);
        assertEquals(AuthorityUser.ANONYMOUS, authorityUser);

        AuthorityUser expected = new AuthorityUser(
                User.builder()
                        .setId(1L)
                        .setLogin("login")
                        .setPhone("phone")
                        .setEmail("email")
                        .setFirstName("first")
                        .setLastName("last")
                        .setRoles(
                                List.of(
                                        Role.builder()
                                                .setId(1L)
                                                .setName("USER")
                                                .setCode("USER")
                                                .createRole()
                                )
                        )
                        .createUser()
        );
        Mockito.when(httpSession.getAttribute(Constants.RequestAttributes
                .USER_ATTRIBUTE)).thenReturn(expected);
        authorityUser = SecurityService.getAuthorityUser(request);
        assertEquals(expected, authorityUser);

    }

    @Test
    void checkIfUserIsLogged() {
        Mockito.when(request.getServletContext()).thenReturn(servletContext);
        Mockito.when(servletContext.getAttribute(Constants
                .RequestAttributes
                .LOGGED_USERS_SET_CONTEXT)).thenReturn(null);
        boolean isLogged = SecurityService.checkIfUserIsLogged(request, "login");
        assertFalse(isLogged);

        Mockito.when(servletContext.getAttribute(Constants
                .RequestAttributes
                .LOGGED_USERS_SET_CONTEXT)).thenReturn(Set.of("login"));
        isLogged = SecurityService.checkIfUserIsLogged(request, "login");
        assertTrue(isLogged);
    }

    @Test
    void addLoggedUserToContext() {
        Mockito.when(request.getServletContext()).thenReturn(servletContext);
        Mockito.when(servletContext.getAttribute(Constants
                .RequestAttributes
                .LOGGED_USERS_SET_CONTEXT)).thenReturn(null);
        Mockito.when(request.getSession()).thenReturn(httpSession);
        AuthorityUser authorityUser = new AuthorityUser(
                User.builder()
                        .setId(1L)
                        .setLogin("login")
                        .setPhone("phone")
                        .setEmail("email")
                        .setFirstName("first")
                        .setLastName("last")
                        .setRoles(
                                List.of(
                                        Role.builder()
                                                .setId(1L)
                                                .setName("USER")
                                                .setCode("USER")
                                                .createRole()
                                )
                        )
                        .createUser()
        );
        SecurityService.addLoggedUserToContext(request, authorityUser);
        Mockito.verify(servletContext).setAttribute(Constants
                .RequestAttributes
                .LOGGED_USERS_SET_CONTEXT, Set.of(authorityUser.getLogin()));
        Mockito.verify(httpSession).setAttribute(Constants
                .RequestAttributes
                .USER_ATTRIBUTE, authorityUser);
    }

    @Test
    void removeLoggedUserFromSession() {
        Mockito.when(request.getSession()).thenReturn(httpSession);
        Mockito.when(request.getServletContext()).thenReturn(servletContext);
        Mockito.when(servletContext.getAttribute(Constants
                .RequestAttributes
                .LOGGED_USERS_SET_CONTEXT)).thenReturn(new HashSet<>(Set.of("login")));
        SecurityService.removeLoggedUserFromSession(request, "login");
        Mockito.verify(httpSession).removeAttribute(Constants
                .RequestAttributes
                .USER_ATTRIBUTE);
        Mockito.verify(servletContext).setAttribute(Constants
                .RequestAttributes
                .LOGGED_USERS_SET_CONTEXT, Set.of());
    }

    @Test
    void removeLoggedUserFromContextAndSession() throws ServiceException {
        Mockito.when(httpSession.getServletContext()).thenReturn(servletContext);
        Mockito.when(httpSession.getAttribute(Constants
                .RequestAttributes
                .USER_ATTRIBUTE)).thenReturn(new AuthorityUser(
                User.builder()
                        .setId(1L)
                        .setLogin("login")
                        .setPhone("phone")
                        .setEmail("email")
                        .setFirstName("first")
                        .setLastName("last")
                        .setRoles(
                                List.of(
                                        Role.builder()
                                                .setId(1L)
                                                .setName("USER")
                                                .setCode("USER")
                                                .createRole()
                                )
                        )
                        .createUser()
                )
        );
        Mockito.when(servletContext.getAttribute(Constants
                .RequestAttributes
                .LOGGED_USERS_SET_CONTEXT)).thenReturn(new HashSet<>(Set.of("login")));
        SecurityService.removeLoggedUserFromContextAndSession(httpSession);
        Mockito.verify(httpSession).removeAttribute(Constants
                .RequestAttributes
                .USER_ATTRIBUTE);
        Mockito.verify(servletContext).setAttribute(Constants
                .RequestAttributes
                .LOGGED_USERS_SET_CONTEXT, Set.of());
    }

    @Test
    void getCurrentLogin() throws ServiceException {
        Mockito.when(request.getSession()).thenReturn(httpSession);
        Mockito.when(httpSession.getAttribute(Constants.RequestAttributes
                .USER_ATTRIBUTE)).thenReturn(null);
        String login;
        assertThrows(ServiceException.class, () -> SecurityService
                .getCurrentLogin(request.getSession()));

        AuthorityUser authorityUser = new AuthorityUser(
                User.builder()
                        .setId(1L)
                        .setLogin("login")
                        .setPhone("phone")
                        .setEmail("email")
                        .setFirstName("first")
                        .setLastName("last")
                        .setRoles(
                                List.of(
                                        Role.builder()
                                                .setId(1L)
                                                .setName("USER")
                                                .setCode("USER")
                                                .createRole()
                                )
                        )
                        .createUser()
        );
        Mockito.when(httpSession.getAttribute(Constants.RequestAttributes
                .USER_ATTRIBUTE)).thenReturn(authorityUser);
        login = SecurityService.getCurrentLogin(request.getSession());
        assertEquals(authorityUser.getLogin(), login);
    }
}