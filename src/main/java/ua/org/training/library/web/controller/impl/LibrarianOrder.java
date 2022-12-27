package ua.org.training.library.web.controller.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private final OrderService orderService = new OrderService();
    private final StatusService statusService = new StatusService();
    private final PlaceService placeService = new PlaceService();
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return createJSONOrderList(request, response);
    }

    @Override
    public void clearRequestSessionAttributes(HttpServletRequest request) {

    }

    private String createJSONOrderList(HttpServletRequest request,
                                       HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding(Constants.APP_ENCODING);
        LOGGER.info("Send json page to user client!");
        PageService<Order> pageService = new PageService<>();
        Page<Order> page = pageService.getPage(request);
        String jsonString = null;
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
}
