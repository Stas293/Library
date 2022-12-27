package ua.org.training.library.web.controller.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

public class AdminNewBook implements ControllerCommand {
    private static final Logger LOGGER = LogManager.getLogger(AdminNewBook.class);
    private final BookService bookService = new BookService();
    private final AuthorService authorService = new AuthorService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String name = Utility.getStringParameter(
                request.getParameter("name"),
                Constants.APP_STRING_DEFAULT_VALUE);
        int count = Utility.tryParseInteger(
                request.getParameter("count"),
                Constants.DEFAULT_BOOK_COUNT);
        String ISBN = Utility.getStringParameter(
                request.getParameter("ISBN"),
                Constants.APP_STRING_DEFAULT_VALUE);
        Date publicationDate = Utility.parseDateOrDefault(
                request.getParameter("publicationDate"),
                Constants.DEFAULT_DATE);
        int fine = Utility.tryParseInteger(
                request.getParameter("fine"),
                Constants.DEFAULT_FINE);
        String language = Utility.getLanguage(
                new Locale(request.getParameter("language")));
        List<Author> authors = Arrays.stream(Utility.getStringParameter(
                                request.getParameter("authors"),
                                Constants.APP_STRING_DEFAULT_VALUE)
                        .split(","))
                .map(Long::parseLong)
                .map((Long id) -> {
                    try {
                        return authorService.getAuthorById(id);
                    } catch (ServiceException e) {
                        LOGGER.error("Service Exception : " + e.getMessage());
                    }
                    return null;
                }).toList();
        Book book = Book.builder()
                .setName(name)
                .setCount(count)
                .setISBN(ISBN)
                .setPublicationDate(publicationDate)
                .setFine(fine)
                .setLanguage(language)
                .setAuthors(authors)
                .createBook();
        try {
            bookService.createBook(book);
        } catch (ServiceException e) {
            LOGGER.error("Service Exception : " + e.getMessage());
            return Links.ADMIN_BOOKS_PAGE_CREATE_ERROR;
        }
        return Links.ADMIN_BOOKS_PAGE_CREATE_SUCCESS;
    }

    @Override
    public void clearRequestSessionAttributes(HttpServletRequest request) {

    }
}
