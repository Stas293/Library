package ua.org.training.library.web.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class LocaleFilterTest {
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    FilterChain filterChain;
    @Mock
    HttpSession session;

    @Test
    void doFilter() {
        LocaleFilter localeFilter = new LocaleFilter();
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(session.getAttribute("lang")).thenReturn("en");
        assertDoesNotThrow(() -> localeFilter.doFilter(request, response, filterChain));

        Mockito.when(request.getParameter("lang")).thenReturn("en");
        Mockito.when(request.getSession()).thenReturn(session);
        assertDoesNotThrow(() -> localeFilter.doFilter(request, response, filterChain));
    }
}