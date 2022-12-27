package ua.org.training.library.web.controller.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.exceptions.ControllerException;
import ua.org.training.library.web.controller.ControllerCommand;

public class ThrowException implements ControllerCommand {
    private static final Logger LOGGER = LogManager.getLogger(ThrowException.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        LOGGER.debug("Throwing exception");
        throw new ControllerException("Throwing exception");
    }

    @Override
    public void clearRequestSessionAttributes(HttpServletRequest request) {

    }
}
