package ua.org.training.library.web.controller.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.context.ApplicationContext;
import ua.org.training.library.exceptions.ConnectionDBException;
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
    private final BookService bookService = ApplicationContext.getInstance().getBookService();
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
        String jsonString = "";
        jsonString = getJSONPage(request, page, jsonString);
        printJSON(response, jsonString);
        return Constants.APP_STRING_DEFAULT_VALUE;
    }

    private static void printJSON(HttpServletResponse response, String jsonString) {
        try {
            PrintWriter writer = response.getWriter();
            writer.print(jsonString);
            LOGGER.info(jsonString);
        } catch (IOException e) {
            LOGGER.error(String.format("Error while writing json to response: %s", e.getMessage()), e);
        }
    }

    private String getJSONPage(HttpServletRequest request, Page<Book> page, String jsonString) {
        try {
            String sortBy = Utility.getStringParameter(
                    request.getParameter(Constants.PARAMETER_SORT_BY),
                    Constants.APP_STRINT_DEFAULT_SORTING);

                jsonString = bookService.getBooksSortedBy(
                                Utility.getLocale(request),
                        page,
                                sortBy);

        } catch (ServiceException e) {
            LOGGER.error(String.format("Service Exception : %s", e.getMessage()), e);
        } catch (ConnectionDBException e) {
            LOGGER.error(String.format("Connection DB Exception : %s", e.getMessage()), e);
        }
        return jsonString;
    }
}
