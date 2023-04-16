package ua.org.training.library.web.filters;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Component;

import java.io.IOException;

@Slf4j
@Component
public class CacheFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        log.debug("Cache filter");
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("Cache-Control", "no-cache,no-store");
        response.setHeader("Pragma", "no-cache");
        filterChain.doFilter(servletRequest, response);
    }
}
