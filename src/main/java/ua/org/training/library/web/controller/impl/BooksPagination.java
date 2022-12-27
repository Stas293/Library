package ua.org.training.library.web.controller.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.model.Book;
import ua.org.training.library.service.BookService;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.PageService;
import ua.org.training.library.web.controller.ControllerCommand;

import java.io.IOException;
import java.io.PrintWriter;

public class BooksPagination implements ControllerCommand {
    private static final Logger LOGGER = LogManager.getLogger(BooksPagination.class);
    private final BookService bookService = new BookService();
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return createJSONBookList(request, response);
    }

    private String createJSONBookList(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding(Constants.APP_ENCODING);
        LOGGER.info("Send json page to user client!");
        PageService<Book> pageService = new PageService<>();
        Page<Book> page = pageService.getPage(request);
        String jsonString = null;
        try {
            String sortBy = Utility.getStringParameter(
                    request.getParameter(Constants.PARAMETER_SORT_BY),
                    Constants.APP_STRING_DEFAULT_VALUE);
            if (sortBy.equals(Constants.APP_STRING_DEFAULT_VALUE)) {
                jsonString = bookService.getBookPage(
                        Utility.getLocale(request),
                        page);
            } else {
                jsonString = bookService.getBooksSortedBy(
                                Utility.getLocale(request),
                                page,
                                sortBy);
            }
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
