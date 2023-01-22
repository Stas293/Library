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
import java.util.List;

public class AdminManipulateBook implements ControllerCommand {
    private static final Logger LOGGER = LogManager.getLogger(AdminManipulateBook.class);
    private final BookService bookService = ApplicationContext.getInstance().getBookService();
    private final AuthorService authorService = ApplicationContext.getInstance().getAuthorService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String[] uriDivided = Utility.getUriParts(request);
        long id;
        LOGGER.info("uriDivided: " + Arrays.toString(uriDivided));

        try {
            if (uriDivided.length == 4 && uriDivided[2].equals("edit")) {
                id = Utility.parseLongOrDefault(uriDivided[3], Constants.APP_DEFAULT_USER_MANIPULATION_ID);
                Book book = getBookById(request, id);
                if (book == null) return Links.ADMIN_BOOKS_PAGE;
                if (request.getMethod().equals("GET")) {
                    return Links.ADMIN_BOOK_EDIT;
                }
                LOGGER.info(
                        request.getParameterMap()
                                .entrySet()
                                .stream()
                                .map(e -> e.getKey() + " : " + Arrays.toString(e.getValue()))
                                .reduce((s1, s2) -> s1 + " " + s2).orElse("empty")
                );
                Book book1 = Mapper.requestDataToBook(request);
                book1.setId(id);
                List<String> authorIds = Mapper.requestDataToAuthorIds(request);
                String adminBooksPage = validateBook(request, book, book1, authorIds);
                if (adminBooksPage != null) return adminBooksPage;
                List<Author> authors;
                authors = getAuthors(authorIds);
                if (authors == null) return Links.ADMIN_BOOKS_PAGE_UPDATE_ERROR;
                book1.setAuthors(authors);
                LOGGER.info(book);
                String adminBooksPageUpdateError = updateBook(book1);
                if (adminBooksPageUpdateError != null) return adminBooksPageUpdateError;
                return Links.ADMIN_BOOKS_PAGE_UPDATE_SUCCESS;

            } else if (uriDivided.length == 4 && uriDivided[2].equals("delete")) {
                id = Utility.parseLongOrDefault(uriDivided[3], Constants.APP_DEFAULT_USER_MANIPULATION_ID);
                String adminBooksPageDeleteError = deleteBook(id);
                if (adminBooksPageDeleteError != null) return adminBooksPageDeleteError;
                return Links.ADMIN_BOOKS_PAGE_DELETE_SUCCESS;
            }
        } catch (ConnectionDBException e) {
            LOGGER.error(e.getMessage(), e);
            return Links.ERROR_PAGE + "?message=" + e.getMessage();
        }
        return Links.ADMIN_AUTHORS_PAGE;
    }

    private String deleteBook(long id) throws ConnectionDBException {
        try {
            bookService.deleteBook(id);
        } catch (ServiceException e) {
            LOGGER.error("Service Exception : " + e.getMessage());
            return Links.ADMIN_BOOKS_PAGE_DELETE_ERROR;
        }
        return null;
    }

    private String updateBook(Book book1) throws ConnectionDBException {
        try {
            bookService.updateBook(book1);
        } catch (ServiceException e) {
            LOGGER.error("Service Exception : " + e.getMessage());
            return Links.ADMIN_BOOKS_PAGE_UPDATE_ERROR;
        }
        return null;
    }

    private List<Author> getAuthors(List<String> authorIds) {
        List<Author> authors;
        try {
            authors = Mapper.authorIdsToAuthors(authorIds, authorService);
        } catch (MapException e) {
            LOGGER.error("Map Exception : " + e.getMessage());
            return null;
        }
        return authors;
    }

    private static String validateBook(HttpServletRequest request, Book book, Book book1, List<String> authorIds) {
        BookValidationError bookValidationError = new BookValidationError();
        try {
            BookValidation bookValidation = ApplicationContext.getInstance().getBookValidation();
            bookValidation.validation(Utility.getLocale(request), book1, bookValidationError);
            bookValidation.validateAuthorIds(authorIds, bookValidationError);
        } catch (UnexpectedValidationException e) {
            LOGGER.error("Unexpected validation exception", e);
            return Links.ADMIN_BOOKS_PAGE;
        }
        if (bookValidationError.isContainsErrors()) {
            request.setAttribute("book", book);
            request.setAttribute("bookValidationError", bookValidationError);
            return Links.RETURN_ADMIN_NEW_BOOK;
        }
        return null;
    }

    private Book getBookById(HttpServletRequest request, long id) throws ConnectionDBException {
        Book book;
        try {
            book = bookService.getBookById(id);
            request.setAttribute("book", book);
        } catch (ServiceException e) {
            LOGGER.error("Error while getting book by id", e);
            return null;
        }
        return book;
    }
}
