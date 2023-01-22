package ua.org.training.library.web.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class CacheFilterTest {
    @Mock
    HttpServletResponse response;
    @Mock
    FilterChain filterChain;

    @Test
    void doFilter() throws ServletException, IOException {
        CacheFilter cacheFilter = new CacheFilter();
        assertDoesNotThrow(() -> cacheFilter.doFilter(null, response, filterChain));
        Mockito.verify(response).setHeader("Cache-Control", "no-cache,no-store");
        Mockito.verify(response).setHeader("Pragma", "no-cache");
        Mockito.verify(filterChain).doFilter(null, response);
    }
}