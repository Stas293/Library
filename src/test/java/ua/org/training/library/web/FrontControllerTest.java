package ua.org.training.library.web;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.org.training.library.model.Role;
import ua.org.training.library.model.User;
import ua.org.training.library.security.AuthorityUser;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.Utility;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FrontControllerTest {
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    HttpSession session;
    @Mock
    RequestDispatcher requestDispatcher;

    @Test
    void doGet() {
        FrontController frontController = new FrontController();
        assertThrows(
                NullPointerException.class,
                () -> frontController.doGet(request, response));

        Mockito.when(request.getRequestURI()).thenReturn("/library/login");
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(request.getRequestDispatcher("/WEB-INF/login.jsp"))
                .thenReturn(requestDispatcher);
        AuthorityUser authorityUser = AuthorityUser.ANONYMOUS;
        Mockito.when(session.getAttribute(Constants.RequestAttributes.USER_ATTRIBUTE)).thenReturn(authorityUser);
        assertDoesNotThrow(() -> frontController.doGet(request, response));
    }

    @Test
    void doPost() throws ServletException, IOException {
        FrontController frontController = new FrontController();
        assertThrows(
                NullPointerException.class,
                () -> frontController.doPost(request, response));

        Mockito.when(request.getRequestURI()).thenReturn("/library/admin/new-author");
        Mockito.when(request.getParameter("firstName")).thenReturn("firstName");
        Mockito.when(request.getParameter("lastName")).thenReturn("lastName");
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(request.getRequestDispatcher(Mockito.anyString()))
                .thenReturn(requestDispatcher);
        assertDoesNotThrow(() -> frontController.doPost(request, response));
    }
}