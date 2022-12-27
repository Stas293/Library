package ua.org.training.library.web.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.utility.Constants;

import java.io.IOException;

@WebFilter(filterName = "LocaleFilter", urlPatterns = {"/*"})
public class LocaleFilter implements Filter {
    private static final Logger LOGGER = LogManager.getLogger(LocaleFilter.class);

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String lang = Constants.RequestAttributes.APP_LANG_ATTRIBUTE;
        if (req.getParameter(lang) != null &&
                !req.getParameter(lang).equals(req.getSession().getAttribute(lang))) {
            req.getSession().setAttribute(lang, req.getParameter(lang));
            LOGGER.debug(String.format("locale was changed on: %s", req.getParameter(lang)));
        }
        chain.doFilter(request, response);
    }
}
