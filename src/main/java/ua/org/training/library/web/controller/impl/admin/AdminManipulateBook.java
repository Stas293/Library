package ua.org.training.library.web.controller.impl.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.context.ApplicationContext;
import ua.org.training.library.exceptions.ConnectionDBException;
import ua.org.training.library.exceptions.MapException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.model.Author;
import ua.org.training.library.model.Book;
import ua.org.training.library.service.AuthorService;
import ua.org.training.library.service.BookService;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.Links;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.web.controller.ControllerCommand;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
                Book book = null;
                try {
                    book = bookService.getBookById(id);
                    request.setAttribute("book", book);
                } catch (ServiceException e) {
                    LOGGER.error("Error while getting book by id", e);
                }
                if (request.getMethod().equals("GET")) {
                    return Links.ADMIN_BOOK_EDIT;
                }
                LOGGER.info(request.getParameterMap().entrySet().stream().map(e -> e.getKey() + " : " + Arrays.toString(e.getValue())).reduce((s1, s2) -> s1 + " " + s2).orElse("empty"));
                String name = Utility.getStringParameter(
                        request.getParameter("name"),
                        Constants.APP_STRING_DEFAULT_VALUE);
                int count = Utility.tryParseInt(
                        request.getParameter("count"),
                        Constants.DEFAULT_BOOK_COUNT);
                String ISBN = Utility.getStringParameter(
                        request.getParameter("ISBN"),
                        Constants.APP_STRING_DEFAULT_VALUE);
                Date publicationDate = Utility.parseDateOrDefault(
                        request.getParameter("publicationDate"),
                        Constants.DEFAULT_DATE);
                double fine = Utility.tryParseDouble(
                        request.getParameter("fine"),
                        Constants.DEFAULT_FINE);
                String language = Utility.getLanguage(
                        new Locale(request.getParameter("language")));
                List<Author> authors;
                try {
                    authors = Arrays.stream(Utility.getStringParameter(
                                            request.getParameter("authors"),
                                            Constants.APP_STRING_DEFAULT_VALUE)
                                    .split(","))
                            .map(Long::parseLong)
                            .map((Long idN) -> {
                                try {
                                    return authorService.getAuthorById(idN);
                                } catch (ServiceException e) {
                                    LOGGER.error("Service Exception : " + e.getMessage());
                                    throw new MapException(e.getMessage(), e);
                                } catch (ConnectionDBException e) {
                                    LOGGER.error("Connection DB Exception : " + e.getMessage());
                                    throw new MapException(e.getMessage(), e);
                                }
                            }).toList();
                } catch (MapException e) {
                    LOGGER.error("Map Exception : " + e.getMessage());
                    return Links.ADMIN_BOOKS_PAGE_UPDATE_ERROR;
                }
                book = Book.builder()
                        .setId(book.getId())
                        .setName(name)
                        .setCount(count)
                        .setISBN(ISBN)
                        .setPublicationDate(publicationDate)
                        .setFine(fine)
                        .setLanguage(language)
                        .setAuthors(authors)
                        .createBook();
                LOGGER.info(book);
                try {
                    bookService.updateBook(book);
                } catch (ServiceException e) {
                    LOGGER.error("Service Exception : " + e.getMessage());
                    return Links.ADMIN_BOOKS_PAGE_UPDATE_ERROR;
                }
                return Links.ADMIN_BOOKS_PAGE_UPDATE_SUCCESS;

            } else if (uriDivided.length == 4 && uriDivided[2].equals("delete")) {
                id = Utility.parseLongOrDefault(uriDivided[3], Constants.APP_DEFAULT_USER_MANIPULATION_ID);
                try {
                    bookService.deleteBook(id);
                } catch (ServiceException e) {
                    LOGGER.error("Service Exception : " + e.getMessage());
                    return Links.ADMIN_BOOKS_PAGE_DELETE_ERROR;
                }
                return Links.ADMIN_BOOKS_PAGE_DELETE_SUCCESS;
            }
        } catch (ConnectionDBException e) {
            LOGGER.error(e.getMessage(), e);
            return Links.ERROR_PAGE + "?message=" + e.getMessage();
        }
        return Links.ADMIN_AUTHORS_PAGE;
    }
}
