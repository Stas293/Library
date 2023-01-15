package ua.org.training.library.web.controller.impl.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.context.ApplicationContext;
import ua.org.training.library.exceptions.ConnectionDBException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.model.Author;
import ua.org.training.library.service.AuthorService;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.Links;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.web.controller.ControllerCommand;

import java.util.Arrays;

public class AdminManipulateAuthor implements ControllerCommand {
    private static final Logger LOGGER = LogManager.getLogger(AdminManipulateAuthor.class);
    private final AuthorService authorService = ApplicationContext.getInstance().getAuthorService();
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String[] uriDivided = Utility.getUriParts(request);
        long id;
        LOGGER.info("uriDivided: " + Arrays.toString(uriDivided));

        try {
            if (uriDivided.length == 4 && uriDivided[2].equals("edit")) {
                id = Utility.parseLongOrDefault(uriDivided[3], Constants.APP_DEFAULT_USER_MANIPULATION_ID);
                Author author = null;
                try {
                    author = authorService.getAuthorById(id);
                    request.setAttribute("author", author);
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
                    return Links.ADMIN_AUTHORS_PAGE_UPDATE_ERROR;
                }
            } else if (uriDivided.length == 4 && uriDivided[2].equals("delete")) {
                id = Utility.parseLongOrDefault(uriDivided[3], Constants.APP_DEFAULT_USER_MANIPULATION_ID);
                try {
                    authorService.deleteAuthor(id);
                    return Links.ADMIN_AUTHORS_PAGE_DELETE_SUCCESS;
                } catch (ServiceException e) {
                    LOGGER.error("Error while deleting author", e);
                    return Links.ADMIN_AUTHORS_PAGE_DELETE_ERROR;
                }
            }
        } catch (ConnectionDBException e) {
            LOGGER.error(e.getMessage(), e);
            return Links.ERROR_PAGE + "?message=" + e.getMessage();
        }
        return Links.ADMIN_AUTHORS_PAGE;
    }
}
