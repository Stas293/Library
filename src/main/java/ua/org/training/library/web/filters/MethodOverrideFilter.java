package ua.org.training.library.web.filters;

import com.project.university.system_library.context.annotations.Autowired;
import com.project.university.system_library.context.annotations.Component;
import com.project.university.system_library.utility.mapper.HttpServletRequestMethodMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MethodOverrideFilter implements Filter {
    private final HttpServletRequestMethodMapper mapper;

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        log.debug("Method override filter");
        HttpServletRequest req = (HttpServletRequest) request;

        log.debug("request method: {}", req.getMethod());
        log.debug("request parameter: {}", req.getParameter("_method"));
        if (req.getMethod().equalsIgnoreCase("POST") && req.getParameter("_method") != null) {
            HttpServletRequest wrapper = mapper.mapToOverrideMethod(req);
            chain.doFilter(wrapper, response);
        } else {
            chain.doFilter(request, response);
        }
    }
}

