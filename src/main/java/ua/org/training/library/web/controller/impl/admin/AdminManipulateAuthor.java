package ua.org.training.library.web.controller.impl.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.context.ApplicationContext;
import ua.org.training.library.exceptions.ConnectionDBException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.form.AuthorValidationError;
import ua.org.training.library.model.Author;
import ua.org.training.library.service.AuthorService;
import ua.org.training.library.utility.Mapper;
import ua.org.training.library.utility.validation.AuthorValidation;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.Links;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.web.controller.ControllerCommand;

import java.util.Arrays;

public class AdminManipulateAuthor implements ControllerCommand {
    private static final Logger LOGGER = LogManager.getLogger(AdminManipulateAuthor.class);
    private final AuthorService authorService = ApplicationContext.getInstance().getAuthorService();
    private final AuthorValidation authorValidation = ApplicationContext.getInstance().getAuthorValidation();
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String[] uriDivided = Utility.getUriParts(request);
        long id;
        LOGGER.info(String.format("uriDivided: %s", Arrays.toString(uriDivided)));

        try {
            if (uriDivided.length == 4 && uriDivided[2].equals("edit")) {
                id = Utility.parseLongOrDefault(uriDivided[3], Constants.APP_DEFAULT_USER_MANIPULATION_ID);
                Author author = getAuthor(request, id);
                LOGGER.info(String.format("author: %s", author));
                if (author == null) return Links.ADMIN_AUTHORS_PAGE;
                if (request.getMethod().equals("GET")) {
                    return Links.ADMIN_AUTHOR_EDIT;
                }
                author = Mapper.requestDataToAuthor(request);
                author.setId(id);
                LOGGER.info(String.format("author: %s", author));
                String correctAuthorData = validateAuthor(request, author);
                if (correctAuthorData != null) return correctAuthorData;
                String adminAuthorsPageUpdateError = updateAuthor(author);
                if (adminAuthorsPageUpdateError != null) return adminAuthorsPageUpdateError;
            } else if (uriDivided.length == 4 && uriDivided[2].equals("delete")) {
                id = Utility.parseLongOrDefault(
                        uriDivided[3],
                        Constants.APP_DEFAULT_USER_MANIPULATION_ID);
                return deleteAuthor(id);
            }
        } catch (ConnectionDBException e) {
            LOGGER.error(e.getMessage(), e);
            return Links.ERROR_PAGE + "?message=" + e.getMessage();
        }
        return Links.ADMIN_AUTHORS_PAGE;
    }

    private String validateAuthor(HttpServletRequest request, Author author) {
        AuthorValidationError authorValidationError = new AuthorValidationError();
        authorValidation.validation(Utility.getLocale(request), author, authorValidationError);
        if (authorValidationError.isContainsErrors()) {
            request.setAttribute("author", author);
            request.setAttribute("authorValidationError", authorValidationError);
            return Links.ADMIN_AUTHOR_EDIT;
        }
        return null;
    }

    private String deleteAuthor(long id) throws ConnectionDBException {
        try {
            authorService.deleteAuthor(id);
            return Links.ADMIN_AUTHORS_PAGE_DELETE_SUCCESS;
        } catch (ServiceException e) {
            LOGGER.error("Error while deleting author", e);
            return Links.ADMIN_AUTHORS_PAGE_DELETE_ERROR;
        }
    }

    private String updateAuthor(Author author) throws ConnectionDBException {
        try {
            authorService.updateAuthor(author);
        } catch (ServiceException e) {
            LOGGER.error("Error while updating author", e);
            return Links.ADMIN_AUTHORS_PAGE_UPDATE_ERROR;
        }
        return null;
    }

    private Author getAuthor(HttpServletRequest request, long id) throws ConnectionDBException {
        Author author;
        try {
            author = authorService.getAuthorById(id);
            request.setAttribute("author", author);
        } catch (ServiceException e) {
            LOGGER.error("Error while getting author by id", e);
            return null;
        }
        return author;
    }
}
