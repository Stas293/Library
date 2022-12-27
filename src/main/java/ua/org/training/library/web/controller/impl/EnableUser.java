package ua.org.training.library.web.controller.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.exceptions.ControllerException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.model.User;
import ua.org.training.library.security.SecurityService;
import ua.org.training.library.service.UserService;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.Links;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.web.controller.ControllerCommand;

public class EnableUser implements ControllerCommand {
    private static final Logger LOGGER = LogManager.getLogger(EnableUser.class);
    private final UserService userService = new UserService();
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        long  id = Utility.parseLongOrDefault(request.getParameter(Constants.RequestAttributes.ACCOUNT_ID_ATTRIBUTE),
                Constants.APP_DEFAULT_ID);
        try {
            User userToDisable = userService.getUserById(id);

            if (!SecurityService.getAuthorityUser(request).hasRole(Constants.APP_ADMIN_ROLE)) {
                LOGGER.error("User can`t manipulate with other users");
                throw new ControllerException("User is not admin");
            }

            userService.enableUserById(userToDisable.getId());
        } catch (ServiceException e) {

            return Links.ADMIN_PAGE_REDIRECT_ENABLE_ACCOUNT_FAILED;
        }
        clearRequestSessionAttributes(request);
        return Links.ADMIN_PAGE_REDIRECT_ENABLE_ACCOUNT_SUCCESS;
    }

    @Override
    public void clearRequestSessionAttributes(HttpServletRequest request) {
        request.getSession().removeAttribute(Constants.RequestAttributes.ACCOUNT_ID_ATTRIBUTE);
    }
}
