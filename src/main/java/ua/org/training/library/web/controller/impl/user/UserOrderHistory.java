package ua.org.training.library.web.controller.impl.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.context.ApplicationContext;
import ua.org.training.library.exceptions.ConnectionDBException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.model.HistoryOrder;
import ua.org.training.library.security.SecurityService;
import ua.org.training.library.service.HistoryOrderService;
import ua.org.training.library.service.UserService;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.PageService;
import ua.org.training.library.web.controller.ControllerCommand;

import java.io.IOException;
import java.io.PrintWriter;

public class UserOrderHistory implements ControllerCommand {
    private static final Logger LOGGER = LogManager.getLogger(UserOrderHistory.class);
    private final HistoryOrderService historyOrderService = ApplicationContext.getInstance().getHistoryOrderService();
    private final UserService userService = ApplicationContext.getInstance().getUserService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return createJSONHistoryOrder(request, response);
    }

    private String createJSONHistoryOrder(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding(Constants.APP_ENCODING);
        LOGGER.info("Sending json page");
        PageService<HistoryOrder> pageService = new PageService<>();
        Page<HistoryOrder> page = pageService.getPage(request);
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
            LOGGER.error(String.format("IO Exception : %s", e.getMessage()));
        }
    }

    private String getJSONPage(HttpServletRequest request, Page<HistoryOrder> page, String jsonString) {
        try {
            jsonString = historyOrderService.getHistoryOrderPageByUserId(
                    Utility.getLocale(request),
                    page,
                    userService.getUserByLogin(
                            SecurityService.getCurrentLogin(request.getSession())).getId());
        } catch (ServiceException e) {
            LOGGER.error(String.format("Service Exception : %s", e.getMessage()));
        } catch (ConnectionDBException e) {
            LOGGER.error(String.format("Connection DB Exception : %s", e.getMessage()));
        }
        return jsonString;
    }
}
