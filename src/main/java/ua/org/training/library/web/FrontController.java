package ua.org.training.library.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.enums.ControllerCommandEnum;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.web.controller.ControllerCommand;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet(name = "FrontController", value = "/library/*")
public class FrontController extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(FrontController.class);
    private transient Set<ControllerCommandEnum> commands;

    @Override
    public void init() {
        commands = Arrays.stream(ControllerCommandEnum.values()).collect(Collectors.toSet());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    private void process(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        LOGGER.info(req.getRequestURI());
        String path = req.getRequestURI().replaceAll(Constants.APP_PATH_REG_EXP, "");
        String page;

        if (path.contains("admin/users")) path = "admin/users";
        else if (path.contains("admin/author/")) {
            path = "admin/author";
        } else if (path.contains("admin/book/")) {
            path = "admin/book";
        }
        LOGGER.info(path);

        path = path.replaceAll("/", "_").replaceAll("-", "_").toUpperCase();

        String finalPath = path;
        ControllerCommand command = commands
                .stream()
                .filter(c -> c.name().equalsIgnoreCase(finalPath))
                .findFirst()
                .orElse(ControllerCommandEnum.LOGIN)
                .getControllerCommand();


        page = command.execute(req, resp);

        if (page.contains("redirect:")) {
            resp.sendRedirect(page.replace("redirect:", "/"));
        } else if (!page.equals("")) {
            req.getRequestDispatcher(page).forward(req, resp);
        }
    }
}
