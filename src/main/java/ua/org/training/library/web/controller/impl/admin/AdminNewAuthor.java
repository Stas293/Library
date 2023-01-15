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
import ua.org.training.library.utility.Links;
import ua.org.training.library.web.controller.ControllerCommand;

public class AdminNewAuthor implements ControllerCommand {
    private static final Logger LOGGER = LogManager.getLogger(AdminNewAuthor.class);
    private final AuthorService authorService = ApplicationContext.getInstance().getAuthorService();
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        Author author = Author.builder()
                .setFirstName(firstName)
                .setLastName(lastName)
                .createAuthor();
        try {
            authorService.createAuthor(author);
        } catch (ServiceException e) {
            LOGGER.error("Service Exception : " + e.getMessage());
            return Links.ADMIN_AUTHORS_PAGE_CREATE_ERROR;
        } catch (ConnectionDBException e) {
            LOGGER.error(e.getMessage(), e);
            return Links.ERROR_PAGE + "?message=" + e.getMessage();
        }
        return Links.ADMIN_AUTHORS_PAGE_CREATE_SUCCESS;
    }
}
