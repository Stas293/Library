package ua.org.training.library.web.controller.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.exceptions.ControllerException;
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
    private final HistoryOrderService historyOrderService = new HistoryOrderService();
    private final UserService userService = new UserService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return createJSONHistoryOrder(request, response);
    }

    private String createJSONHistoryOrder(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding(Constants.APP_ENCODING);
        LOGGER.info("Send json page to user client!");
        PageService<HistoryOrder> pageService = new PageService<>();
        Page<HistoryOrder> page = pageService.getPage(request);
        String jsonString;
        try {
            jsonString = historyOrderService.getHistoryOrderPageByUserId(
                    Utility.getLocale(request),
                    page,
                    userService.getUserByLogin(
                            SecurityService.getCurrentLogin(request.getSession())).getId());
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
        try {
            PrintWriter writer = response.getWriter();
            writer.print(jsonString);
            LOGGER.info(jsonString);
        } catch (IOException e) {
            LOGGER.error(String.format("IO Exception : %s", e.getMessage()));
        }
        return Constants.APP_STRING_DEFAULT_VALUE;
    }

    @Override
    public void clearRequestSessionAttributes(HttpServletRequest request) {
    }
}
