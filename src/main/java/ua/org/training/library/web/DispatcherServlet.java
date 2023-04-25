package ua.org.training.library.web;


import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.HttpMapping;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.utility.mapper.HttpServletRequestMethodMapper;

import java.io.IOException;
import java.lang.invoke.MethodHandle;

@Component
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DispatcherServlet extends HttpServlet {
    private final ControllerFactory controllerFactory;
    private final HttpServletRequestMethodMapper mapper;

    @Override
    @SneakyThrows
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("Request URI: {}", req.getRequestURI());
        String httpMethod = req.getMethod();
        String httpPath = req.getRequestURI();
        try {
            HttpMapping mapping = controllerFactory.getControllerCommand(httpMethod, httpPath);
            log.info("Mapping: {}", mapping);
            MethodHandle method = mapping.method();
            log.info("Method: {}", method);
            Object page = method.invoke(mapping.controller(), req, resp);
            log.info("Page: {}", page);
            String pageString = page.toString();
            if (pageString.contains("redirect:")) {
                resp.sendRedirect(pageString.replace("redirect:", "/"));
            } else if (!page.equals("")) {
                req = mapper.checkMethodOverride(req);
                req.getRequestDispatcher(pageString).forward(req, resp);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resp.sendRedirect("/error");
        }
    }
}
