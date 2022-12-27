package ua.org.training.library.web.controller.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ua.org.training.library.utility.Links;
import ua.org.training.library.web.controller.ControllerCommand;

public class AdminAddBook implements ControllerCommand {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return Links.ADMIN_ADD_BOOK;
    }

    @Override
    public void clearRequestSessionAttributes(HttpServletRequest request) {

    }
}
