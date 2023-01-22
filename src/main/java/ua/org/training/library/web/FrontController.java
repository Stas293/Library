package ua.org.training.library.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.web.controller.ControllerCommand;
import ua.org.training.library.web.controller_factory.ControllerCommandFactory;

import java.io.IOException;

@WebServlet(name = "FrontController", value = "/library/*")
public class FrontController extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(FrontController.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.info(String.format("GET request URI: %s", req.getRequestURI()));
        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.info(String.format("POST request URI: %s", req.getRequestURI()));
        process(req, resp);
    }

    private void process(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        ControllerCommand command = ControllerCommandFactory.getCommand(req.getRequestURI());
        LOGGER.info(String.format("Command: %s", command.getClass().getSimpleName()));
        try {
            String page = command.execute(req, resp);
            LOGGER.info(page);
            if (page.contains("redirect:")) {
                resp.sendRedirect(page.replace("redirect:", "/"));
            } else if (!page.equals("")) {
                req.getRequestDispatcher(page).forward(req, resp);
            }
        } catch (ExceptionInInitializerError e) {
            LOGGER.error(e.getMessage(), e);
            resp.sendRedirect("/library/error?failedconnection=true");
        }
    }
}
