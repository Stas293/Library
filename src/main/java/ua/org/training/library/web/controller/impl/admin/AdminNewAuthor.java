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
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.Links;
import ua.org.training.library.utility.Mapper;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.utility.validation.AuthorValidation;
import ua.org.training.library.web.controller.ControllerCommand;

import java.util.Locale;

public class AdminNewAuthor implements ControllerCommand {
    private static final Logger LOGGER = LogManager.getLogger(AdminNewAuthor.class);
    private final AuthorService authorService;
    private final AuthorValidation authorValidation;

    public AdminNewAuthor() {
        authorService = ApplicationContext.getInstance().getAuthorService();
        authorValidation = ApplicationContext.getInstance().getAuthorValidation();
    }

    public AdminNewAuthor(AuthorService authorService, AuthorValidation authorValidation) {
        this.authorService = authorService;
        this.authorValidation = authorValidation;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Author author = Mapper.requestDataToAuthor(request);
        LOGGER.info(String.format("author: %s", author));
        AuthorValidationError authorValidationError = new AuthorValidationError();
        authorValidation.validation(Utility.getLocale(request), author, authorValidationError);
        if (authorValidationError.isContainsErrors()) {
            request.setAttribute("authorValidationError", authorValidationError);
            return Links.ADMIN_AUTHORS_PAGE_ERROR;
        }
        try {
            authorService.createAuthor(author);
        } catch (ServiceException e) {
            LOGGER.error(String.format("Error while creating author: %s", author), e);
            return Links.ADMIN_AUTHORS_PAGE_CREATE_ERROR;
        } catch (ConnectionDBException e) {
            LOGGER.error(String.format("Error while creating author: %s", author), e);
            return Links.ERROR_PAGE + "?message=" + e.getMessage();
        }
        return Links.ADMIN_AUTHORS_PAGE_CREATE_SUCCESS;
    }
}
