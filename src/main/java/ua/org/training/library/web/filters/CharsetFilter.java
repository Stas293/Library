package ua.org.training.library.web.filters;

import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.enums.DefaultValues;
import ua.org.training.library.context.annotations.Component;

import java.io.IOException;

@Slf4j
@Component
public class CharsetFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        log.debug("Charset filter");
        servletRequest.setCharacterEncoding(DefaultValues.APP_ENCODING.getValue());
        servletResponse.setCharacterEncoding(DefaultValues.APP_ENCODING.getValue());
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
