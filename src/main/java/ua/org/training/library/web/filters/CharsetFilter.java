package ua.org.training.library.web.filters;

import com.project.university.system_library.constants.Values;
import com.project.university.system_library.context.annotations.Component;
import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@Component
public class CharsetFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        log.debug("Charset filter");
        servletRequest.setCharacterEncoding(Values.APP_ENCODING);
        servletResponse.setCharacterEncoding(Values.APP_ENCODING);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
