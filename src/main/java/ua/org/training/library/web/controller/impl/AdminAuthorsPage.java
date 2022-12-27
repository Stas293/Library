package ua.org.training.library.web.controller.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private final AuthorService authorService = new AuthorService();
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return createJSONAuthorList(request, response);
    }

    private String createJSONAuthorList(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding(Constants.APP_ENCODING);
        LOGGER.info("Send json page to user client!");
        PageService<Author> pageService = new PageService<>();
        Page<Author> page = pageService.getPage(request);
        String jsonString = null;
        try {
            jsonString = authorService.getAuthorPage(page);
        } catch (ServiceException e) {
            LOGGER.error("Service Exception : " + e.getMessage());
        }
        try {
            PrintWriter writer = response.getWriter();
            writer.print(jsonString);
            LOGGER.info(jsonString);
        } catch (IOException e) {
            LOGGER.error("IO Exception : " + e.getMessage());
        }
        return Constants.APP_STRING_DEFAULT_VALUE;
    }

    @Override
    public void clearRequestSessionAttributes(HttpServletRequest request) {

    }
}
