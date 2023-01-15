package ua.org.training.library.web.controller.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;
import ua.org.training.library.context.ApplicationContext;
import ua.org.training.library.exceptions.ConnectionDBException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.exceptions.UnexpectedValidationException;
import ua.org.training.library.form.FormValidationError;
import ua.org.training.library.model.User;
import ua.org.training.library.security.AuthorityUser;
import ua.org.training.library.security.SecurityService;
import ua.org.training.library.service.UserService;
import ua.org.training.library.service.UserValidation;
import ua.org.training.library.utility.DTOMapper;
import ua.org.training.library.utility.Links;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.web.controller.ControllerCommand;

public class EditPersonalData implements ControllerCommand {
    private static final Logger LOGGER = LogManager.getLogger(EditPersonalData.class);

    private final UserService userService = ApplicationContext.getInstance().getUserService();
    private final UserValidation userValidation = new UserValidation(userService);

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response) {

        if (request.getMethod().equals("GET")) {
            LOGGER.debug("Form was sent by GET method");
            try {
                User user = userService.getUserByLogin(SecurityService.getCurrentLogin(request.getSession()));
                request.setAttribute("account", DTOMapper.userDTO(user));
            } catch (ServiceException e) {
                LOGGER.error("Error while getting user by login", e);
                return Links.ERROR_CHANGE_PERSONAL_DATA_PAGE;
            } catch (ConnectionDBException e) {
                LOGGER.error(e.getMessage(), e);
                return Links.ERROR_PAGE + "?message=" + e.getMessage();
            }
            return Links.EDIT_PERSONAL_DATA_PAGE;
        } else {
            FormValidationError formErrors = new FormValidationError();

            User initialUser = null;
            try {
                initialUser = userService.getUserByLogin(SecurityService.getCurrentLogin(request.getSession()));
            } catch (ServiceException e) {
                LOGGER.error("Error while getting user by login", e);
                return Links.ERROR_CHANGE_PERSONAL_DATA_PAGE;
            } catch (ConnectionDBException e) {
                LOGGER.error(e.getMessage(), e);
                return Links.ERROR_PAGE + "?message=" + e.getMessage();
            }

            User user = userService.collectUserFromRequest(request);
            user.setId(initialUser.getId());
            user.setRoles(initialUser.getRoles());

            try {
                userValidation.validation(Utility.getLocale(request),
                        user,
                        formErrors);
            } catch (UnexpectedValidationException e) {
                LOGGER.error("Unexpected validation error", e);
                return Links.ERROR_PAGE + "?message=" + e.getMessage();
            }
            LOGGER.debug("error has formErrors = " + formErrors.isContainsErrors());

            if (!formErrors.isContainsErrors())
                try {
                    userService.updateUserData(user);
                    SecurityService.updateUserDataInContext(
                            request,
                            new AuthorityUser(user),
                            SecurityService.getAuthorityUser(request));
                } catch (ServiceException e) {
                    LOGGER.error("Error while creating user", e);
                    return Links.ERROR_EDIT_PERSONAL_DATA;
                } catch (ConnectionDBException e) {
                    LOGGER.error("Error while creating user", e);
                    return Links.ERROR_PAGE + "?message=" + e.getMessage();
                }
            request.setAttribute("account", user);
            request.setAttribute("errors", formErrors);

            if (formErrors.isContainsErrors()) {
                return Links.EDIT_PERSONAL_DATA_PAGE;
            }
            request.removeAttribute("account");
            request.removeAttribute("errors");
            return Links.EDIT_PERSONAL_DATA_SUCCESS_PAGE;
        }
    }
}
