package ua.org.training.library.web.controller.impl.librarian;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.context.ApplicationContext;
import ua.org.training.library.exceptions.ConnectionDBException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.model.Order;
import ua.org.training.library.service.OrderService;
import ua.org.training.library.service.PlaceService;
import ua.org.training.library.service.StatusService;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.PageService;
import ua.org.training.library.web.controller.ControllerCommand;

import java.io.IOException;
import java.io.PrintWriter;

public class LibrarianOrder implements ControllerCommand {
    private static final Logger LOGGER = LogManager.getLogger(LibrarianOrder.class);
    private final OrderService orderService = ApplicationContext.getInstance().getOrderService();
    private final StatusService statusService = ApplicationContext.getInstance().getStatusService();
    private final PlaceService placeService = ApplicationContext.getInstance().getPlaceService();
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return createJSONOrderList(request, response);
    }

    private String createJSONOrderList(HttpServletRequest request,
                                       HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding(Constants.APP_ENCODING);
        LOGGER.info("Sending JSON order list");
        PageService<Order> pageService = new PageService<>();
        Page<Order> page = pageService.getPage(request);
        String jsonString = "";
        jsonString = getJsonString(request, page, jsonString);
        printString(response, jsonString);
        return Constants.APP_STRING_DEFAULT_VALUE;
    }

    private static void printString(HttpServletResponse response, String jsonString) {
        try {
            PrintWriter writer = response.getWriter();
            writer.print(jsonString);
            LOGGER.info(jsonString);
        } catch (IOException e) {
            LOGGER.error(String.format("Error while getting order page by status id: %s",
                    e.getMessage()));
        }
    }

    private String getJsonString(HttpServletRequest request, Page<Order> page, String jsonString) {
        try {
            if (request.getParameter(Constants.RequestAttributes.PLACE_NAME_ATTRIBUTE) != null) {
                jsonString = orderService
                        .getOrderPageByStatusIdAndPlaceId(Utility.getLocale(request),
                                page,
                                statusService.getByCode(
                                        Utility.getStringParameter(
                                                request.getParameter(Constants.STATUS_CODE_PARAMETER),
                                                Constants.STATUS_CODE_REGISTERED)).getId(),
                                placeService.getPlaceByName(
                                        Utility.getStringParameter(
                                                request.getParameter(Constants.RequestAttributes.PLACE_NAME_ATTRIBUTE),
                                                Constants.APP_STRING_DEFAULT_VALUE)).getId(),
                                Utility.getStringParameter(
                                        request.getParameter(Constants.PARAMETER_SORT_BY),
                                        Constants.DEFAULT_ORDER_SORT_BY));
            } else {
                jsonString = orderService
                        .getOrderPageByStatusId(Utility.getLocale(request),
                                page,
                                statusService.getByCode(
                                        Utility.getStringParameter(
                                                request.getParameter(Constants.STATUS_CODE_PARAMETER),
                                                Constants.STATUS_CODE_REGISTERED)).getId(),
                                Utility.getStringParameter(
                                        request.getParameter(Constants.PARAMETER_SORT_BY),
                                        Constants.DEFAULT_ORDER_SORT_BY));
            }
        } catch (ServiceException | ConnectionDBException e) {
            LOGGER.error(String.format("Error while getting order page by status id: %s",
                    e.getMessage()));
        }
        return jsonString;
    }
}
