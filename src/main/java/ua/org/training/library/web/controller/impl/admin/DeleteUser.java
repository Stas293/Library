package ua.org.training.library.web.controller.impl.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.context.ApplicationContext;
import ua.org.training.library.exceptions.ConnectionDBException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.model.User;
import ua.org.training.library.security.SecurityService;
import ua.org.training.library.service.UserService;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.Links;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.web.controller.ControllerCommand;

public class DeleteUser implements ControllerCommand {
    private static final Logger LOGGER = LogManager.getLogger(DeleteUser.class);
    private final UserService userService = ApplicationContext.getInstance().getUserService();
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        long  id = Utility.parseLongOrDefault(request.getParameter(Constants.RequestAttributes.ACCOUNT_ID_ATTRIBUTE),
                Constants.APP_DEFAULT_ID);
        try {
            User userToDelete = userService.getUserById(id);

            if (!SecurityService.getAuthorityUser(request).hasRole(Constants.APP_ADMIN_ROLE)) {
                LOGGER.error("User can`t manipulate with other users");
                return Links.ACCESS_DENIED_PAGE;
            }

            userService.deleteUser(userToDelete.getId());
        } catch (ServiceException e) {
            return Links.ADMIN_PAGE_REDIRECT_DELETE_ACCOUNT_FAILED;
        } catch (ConnectionDBException e) {
            LOGGER.error(e.getMessage(), e);
            return Links.ERROR_PAGE + "?message=" + e.getMessage();
        }
        return Links.ADMIN_PAGE_REDIRECT_DELETE_ACCOUNT_SUCCESS;
    }
}
