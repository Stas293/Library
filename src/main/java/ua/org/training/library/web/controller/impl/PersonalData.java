package ua.org.training.library.web.controller.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.model.User;
import ua.org.training.library.security.SecurityService;
import ua.org.training.library.service.UserService;
import ua.org.training.library.utility.Links;
import ua.org.training.library.web.controller.ControllerCommand;

public class PersonalData implements ControllerCommand {
    private static final Logger LOGGER = LogManager.getLogger(PersonalData.class);
    private final UserService userService = new UserService();
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            User user = userService.getUserByLogin(
                    SecurityService.getAuthorityUser(request).getLogin());
            request.getSession().setAttribute("authority", user);
        } catch (ServiceException e) {
            LOGGER.error("Error while getting user by login", e);
        }
        return Links.PERSONAL_DATA;
    }

    @Override
    public void clearRequestSessionAttributes(HttpServletRequest request) {

    }
}
