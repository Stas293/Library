package ua.org.training.library.utility.mapper;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.constants.Values;
import ua.org.training.library.context.annotations.Component;

import java.io.Serializable;

@Component
@Slf4j
public class HttpServletRequestMethodMapper implements Serializable {
    public HttpServletRequest mapToOverrideMethod(HttpServletRequest request) {
        log.info("Mapping to override method");
        String method = request.getParameter("_method");
        log.info("Method: " + method);
        if (method != null) {
            request = new HttpServletRequestWrapper(request) {
                @Override
                public String getMethod() {
                    return method;
                }
            };
        }
        return request;
    }

    public HttpServletRequest checkMethodOverride(HttpServletRequest req) {
        log.info("Checking method override");
        if (req.getParameter("_method") == null) {
            return req;
        }
        log.info("Method override");
        return new HttpServletRequestWrapper(req) {
            @Override
            public String getMethod() {
                return Values.POST_METHOD;
            }
        };
    }
}
