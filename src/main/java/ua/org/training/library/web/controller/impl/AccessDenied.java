package ua.org.training.library.web.controller.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ua.org.training.library.utility.Links;
import ua.org.training.library.web.controller.ControllerCommand;

public class AccessDenied implements ControllerCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return Links.ACCESS_DENIED_PAGE;
    }
}
