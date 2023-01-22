package ua.org.training.library.web.controller.impl.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.context.ApplicationContext;
import ua.org.training.library.exceptions.ConnectionDBException;
import ua.org.training.library.exceptions.MapException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.exceptions.UnexpectedValidationException;
import ua.org.training.library.form.BookValidationError;
import ua.org.training.library.model.Author;
import ua.org.training.library.model.Book;
import ua.org.training.library.service.AuthorService;
import ua.org.training.library.service.BookService;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.Links;
import ua.org.training.library.utility.Mapper;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.utility.validation.BookValidation;
import ua.org.training.library.web.controller.ControllerCommand;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdminNewBook implements ControllerCommand {
    private static final Logger LOGGER = LogManager.getLogger(AdminNewBook.class);
    private final BookService bookService = ApplicationContext.getInstance().getBookService();
    private final AuthorService authorService = ApplicationContext.getInstance().getAuthorService();
    private final BookValidation bookValidation = ApplicationContext.getInstance().getBookValidation();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Book book = Mapper.requestDataToBook(request);
        LOGGER.info(String.format("book: %s", book));
        List<String> authorIds = Mapper.requestDataToAuthorIds(request);
        LOGGER.info(String.format("authorIds: %s", authorIds));
        String redirectAdminNewBook = validateBook(request, book, authorIds);
        if (redirectAdminNewBook != null) return redirectAdminNewBook;
        List<Author> authors;
        try {
            authors = Mapper.authorIdsToAuthors(authorIds, authorService);
        } catch (MapException e) {
            LOGGER.error(e.getMessage(), e);
            return Links.ERROR_PAGE + "?message=" + e.getMessage();
        }
        book.setAuthors(authors);
        try {
            bookService.createBook(book);
        } catch (ServiceException e) {
            LOGGER.error(String.format("Can't create book: %s", book), e);
            return Links.ADMIN_BOOKS_PAGE_CREATE_ERROR + "?message=" + e.getMessage();
        } catch (ConnectionDBException e) {
            LOGGER.error(e.getMessage(), e);
            return Links.ERROR_PAGE + "?message=" + e.getMessage();
        }
        return Links.ADMIN_BOOKS_PAGE_CREATE_SUCCESS;
    }

    private String validateBook(HttpServletRequest request, Book book, List<String> authorIds) {
        BookValidationError bookValidationError = new BookValidationError();
        try {
            bookValidation.validation(Utility.getLocale(request), book, bookValidationError);
            bookValidation.validateAuthorIds(authorIds, bookValidationError);
        } catch (UnexpectedValidationException e) {
            LOGGER.error(e.getMessage());
            return Links.REDIRECT_ADMIN_NEW_BOOK;
        }
        if (bookValidationError.isContainsErrors()) {
            request.setAttribute("book", book);
            request.setAttribute("bookValidationError", bookValidationError);
            return Links.RETURN_ADMIN_NEW_BOOK;
        }
        return null;
    }
}
