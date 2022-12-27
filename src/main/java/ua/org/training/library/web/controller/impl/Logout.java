package ua.org.training.library.web.controller.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.security.AuthorityUser;
import ua.org.training.library.security.SecurityService;
import ua.org.training.library.utility.Links;
import ua.org.training.library.web.controller.ControllerCommand;

public class Logout implements ControllerCommand {
    private static final Logger LOGGER = LogManager.getLogger(Logout.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        AuthorityUser authorityUser = SecurityService.getAuthorityUser(request);
        SecurityService.removeLoggedUserFromSession(request, authorityUser.getLogin());
        LOGGER.info("User {} was logged out", authorityUser.getLogin());
        return Links.MAIN_PAGE;
    }

    @Override
    public void clearRequestSessionAttributes(HttpServletRequest request) {

    }
}
