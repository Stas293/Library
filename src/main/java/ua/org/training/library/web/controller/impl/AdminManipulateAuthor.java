package ua.org.training.library.web.controller.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.exceptions.ControllerException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.model.Author;
import ua.org.training.library.model.Role;
import ua.org.training.library.model.User;
import ua.org.training.library.service.AuthorService;
import ua.org.training.library.service.UserService;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.Links;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.web.controller.ControllerCommand;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class AdminManipulateAuthor implements ControllerCommand {
    private static final Logger LOGGER = LogManager.getLogger(AdminManipulateAuthor.class);
    private final AuthorService authorService = new AuthorService();
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String[] uriDivided = Utility.getUriParts(request);
        long id;
        LOGGER.info("uriDivided: " + Arrays.toString(uriDivided));

        if (uriDivided.length == 4 && uriDivided[2].equals("edit")) {
            id = Utility.parseLongOrDefault(uriDivided[3], Constants.APP_DEFAULT_USER_MANIPULATION_ID);
            Author author = null;
            try {
                author = authorService.getAuthorById(id);
                session.setAttribute("author", author);
            } catch (ServiceException e) {
                LOGGER.error("Error while getting author by id", e);
            }
            if (request.getMethod().equals("GET")) {
                return Links.ADMIN_AUTHOR_EDIT;
            }
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            author.setFirstName(firstName);
            author.setLastName(lastName);
            try {
                authorService.updateAuthor(author);
            } catch (ServiceException e) {
                LOGGER.error("Error while updating author", e);
            }
        } else if (uriDivided.length == 4 && uriDivided[2].equals("delete")) {
            id = Utility.parseLongOrDefault(uriDivided[3], Constants.APP_DEFAULT_USER_MANIPULATION_ID);
            try {
                authorService.deleteAuthor(id);
            } catch (ServiceException e) {
                LOGGER.error("Error while deleting author", e);
            }
        }
        return Links.ADMIN_AUTHORS_PAGE;
    }

    @Override
    public void clearRequestSessionAttributes(HttpServletRequest request) {

    }
}
