package ua.org.training.library.web.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import ua.org.training.library.utility.Constants;

import java.io.IOException;

@WebFilter(filterName = "CharsetFilter", urlPatterns = {"/*"})
public class CharsetFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding(Constants.APP_ENCODING);
        servletResponse.setCharacterEncoding(Constants.APP_ENCODING);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
