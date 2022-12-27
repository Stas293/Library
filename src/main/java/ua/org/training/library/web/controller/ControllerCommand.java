package ua.org.training.library.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ua.org.training.library.exceptions.ControllerException;

public interface ControllerCommand {
    String execute(HttpServletRequest request, HttpServletResponse response);

    void clearRequestSessionAttributes(HttpServletRequest request);
}
