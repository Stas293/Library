package ua.org.training.library.web.controller.impl.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ua.org.training.library.utility.Links;
import ua.org.training.library.web.controller.ControllerCommand;

public class AdminBooks implements ControllerCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return Links.ADMIN_BOOKS_PAGE;
    }
}
