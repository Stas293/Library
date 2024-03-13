package ua.org.training.library.web.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class CharsetFilterTest {
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    FilterChain filterChain;

    @Test
    void doFilter() throws IOException, ServletException {
        CharsetFilter charsetFilter = new CharsetFilter();
        assertDoesNotThrow(() -> charsetFilter.doFilter(request, response, filterChain));
        Mockito.verify(request).setCharacterEncoding("UTF-8");
        Mockito.verify(response).setCharacterEncoding("UTF-8");
        Mockito.verify(filterChain).doFilter(request, response);
    }
}