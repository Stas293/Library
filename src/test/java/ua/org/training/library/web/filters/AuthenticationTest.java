package ua.org.training.library.web.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationTest {
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    FilterChain filterChain;
    @Mock
    HttpSession session;
    @Mock
    ServletContext servletContext;

    @Test
    void doFilter() throws ServletException, IOException {
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(request.getRequestURI()).thenReturn("/library/login");
        Authentication authentication = new Authentication();
        assertDoesNotThrow(() -> authentication.doFilter(request, response, filterChain));
        Mockito.verify(filterChain).doFilter(request, response);
        Mockito.verify(request, Mockito.never()).getContextPath();
        Mockito.verify(request, Mockito.never()).setAttribute(Mockito.anyString(), Mockito.anyString());

        Mockito.when(request.getRequestURI()).thenReturn("/library/admin");
        Mockito.when(session.getAttribute("user")).thenReturn(null);
        Mockito.when(request.getServletContext()).thenReturn(servletContext);
        assertDoesNotThrow(() -> authentication.doFilter(request, response, filterChain));
        Mockito.verify(request).setAttribute(Mockito.anyString(), Mockito.anyString());
    }
}