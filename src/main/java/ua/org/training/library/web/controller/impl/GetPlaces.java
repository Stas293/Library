package ua.org.training.library.web.controller.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.context.ApplicationContext;
import ua.org.training.library.exceptions.ConnectionDBException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.model.Order;
import ua.org.training.library.model.Place;
import ua.org.training.library.service.PlaceService;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.PageService;
import ua.org.training.library.web.controller.ControllerCommand;

import java.io.IOException;
import java.io.PrintWriter;

public class GetPlaces implements ControllerCommand {
    private static final Logger LOGGER = LogManager.getLogger(GetPlaces.class);
    private final PlaceService placeService = ApplicationContext.getInstance().getPlaceService();
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return createJSONPlaceList(request, response);
    }

    private String createJSONPlaceList(HttpServletRequest request,
                                       HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding(Constants.APP_ENCODING);
        LOGGER.info("Send json page to user client!");
        String jsonString = "";
        jsonString = getJsonString(request, jsonString);
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

    private String getJsonString(HttpServletRequest request, String jsonString) {
        try {
            jsonString = placeService.getPlaceList(
                    Utility.getLocale(request));
        } catch (ServiceException e) {
            LOGGER.error(String.format("Service Exception : %s", e.getMessage()));
        } catch (ConnectionDBException e) {
            LOGGER.error(String.format("Connection DB Exception : %s", e.getMessage()));
        }
        return jsonString;
    }
}
