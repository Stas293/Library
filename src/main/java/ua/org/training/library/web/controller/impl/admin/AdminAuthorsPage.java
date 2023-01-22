package ua.org.training.library.web.controller.impl.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.context.ApplicationContext;
import ua.org.training.library.exceptions.ConnectionDBException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.model.Author;
import ua.org.training.library.service.AuthorService;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.PageService;
import ua.org.training.library.web.controller.ControllerCommand;

import java.io.IOException;
import java.io.PrintWriter;

public class AdminAuthorsPage implements ControllerCommand {
    private static final Logger LOGGER = LogManager.getLogger(AdminAuthorsPage.class);
    private final AuthorService authorService = ApplicationContext.getInstance().getAuthorService();
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return createJSONAuthorList(request, response);
    }

    private String createJSONAuthorList(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding(Constants.APP_ENCODING);
        LOGGER.info("Sending json to user");
        PageService<Author> pageService = new PageService<>();
        Page<Author> page = pageService.getPage(request);
        String jsonString = "";
        jsonString = getAuthorPage(page, jsonString);
        printJSON(response, jsonString);
        return Constants.APP_STRING_DEFAULT_VALUE;
    }

    private static void printJSON(HttpServletResponse response, String jsonString) {
        try {
            PrintWriter writer = response.getWriter();
            writer.print(jsonString);
            LOGGER.info(jsonString);
        } catch (IOException e) {
            LOGGER.error(String.format("Error while sending json to user: %s", e.getMessage()));
        }
    }

    private String getAuthorPage(Page<Author> page, String jsonString) {
        try {
            jsonString = authorService.getAuthorPage(page);
        } catch (ServiceException e) {
            LOGGER.error(String.format("Service Exception : %s", e.getMessage()));
        } catch (ConnectionDBException e) {
            LOGGER.error(String.format("ConnectionDB Exception : %s", e.getMessage()));
        }
        return jsonString;
    }
}
