package ua.org.training.library.web.filters;

import com.project.university.system_library.constants.Values;
import com.project.university.system_library.context.annotations.Component;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@Component
public class LocaleFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        log.debug("Locale filter");
        HttpServletRequest req = (HttpServletRequest) request;
        String lang = Values.APP_LANG_ATTRIBUTE;
        log.debug(String.format("locale was: %s", req.getSession().getAttribute(lang)));
        if (req.getParameter(lang) != null &&
                !req.getParameter(lang).equals(req.getSession().getAttribute(lang))) {
            req.getSession().setAttribute(lang, req.getParameter(lang));
            log.debug(String.format("locale was changed to: %s", req.getParameter(lang)));
        }
        chain.doFilter(request, response);
    }
}
