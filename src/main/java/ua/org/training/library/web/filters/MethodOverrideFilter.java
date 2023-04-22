package ua.org.training.library.web.filters;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.utility.mapper.HttpServletRequestMethodMapper;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MethodOverrideFilter implements Filter {
    private final HttpServletRequestMethodMapper mapper;
    private static final String POST_METHOD = "POST";

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        log.debug("Method override filter");
        HttpServletRequest req = (HttpServletRequest) request;

        log.debug("request method: {}", req.getMethod());
        log.debug("request parameter: {}", req.getParameter("_method"));
        if (req.getMethod().equalsIgnoreCase(POST_METHOD) && req.getParameter("_method") != null) {
            HttpServletRequest wrapper = mapper.mapToOverrideMethod(req);
            chain.doFilter(wrapper, response);
        } else {
            chain.doFilter(request, response);
        }
    }
}

