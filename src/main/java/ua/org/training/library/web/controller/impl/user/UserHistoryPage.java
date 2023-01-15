package ua.org.training.library.web.controller.impl.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ua.org.training.library.utility.Links;
import ua.org.training.library.web.controller.ControllerCommand;

public class UserHistoryPage implements ControllerCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return Links.USER_HISTORY_PAGE;
    }
}
