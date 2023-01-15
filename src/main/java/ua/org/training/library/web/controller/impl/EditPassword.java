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
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.Links;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.web.controller.ControllerCommand;

import java.util.Collections;

public class EditPassword implements ControllerCommand {
    private static final Logger LOGGER = LogManager.getLogger(EditPassword.class);
    private final UserService userService = ApplicationContext.getInstance().getUserService();
    private final UserValidation userValidation = new UserValidation(userService);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        if (request.getMethod().equals("GET")) {
            LOGGER.debug("Form was sent by GET method");
            return Links.PASSWORD_EDIT_PAGE;
        } else {

            FormValidationError formErrors = new FormValidationError();

            LOGGER.debug("FormValidator: " + formErrors);

            String password = Utility.getStringParameter(
                    request.getParameter(
                            Constants.RequestAttributes.APP_PASSWORD_ATTRIBUTE),
                    Constants.APP_STRING_DEFAULT_VALUE);
            String confirmPassword = Utility.getStringParameter(
                    request.getParameter(
                            Constants.RequestAttributes.ACCOUNT_CONFIRM_PASSWORD_ATTRIBUTE),
                    Constants.APP_STRING_DEFAULT_VALUE);
            LOGGER.debug(String.format("User password: %s, confirm password: %s", password, confirmPassword));

            userValidation.validatePasswordLength(password, formErrors);
            userValidation.validatePasswordLength(confirmPassword, formErrors);
            userValidation.validateConfirmPassword(
                    password,
                    confirmPassword,
                    formErrors);

            LOGGER.debug("error has formErrors = " + formErrors.isContainsErrors());

            if (!formErrors.isContainsErrors())
                try {
                    String bcryptPassword = BCrypt.hashpw(
                            password, BCrypt.gensalt(Constants.APP_BCRYPT_SALT));
                    userService.updateUserPassword(
                            userService.getUserByLogin(
                                    SecurityService.getCurrentLogin(request.getSession())
                            ),
                            bcryptPassword
                    );
                } catch (ServiceException e) {
                    LOGGER.error("Error while creating user", e);
                    return Links.PASSWORD_EDIT_PAGE_FAIL;
                } catch (ConnectionDBException e) {
                    LOGGER.error("Error while creating user", e);
                    return Links.ERROR_PAGE + "?message=" + e.getMessage();
                }
            request.setAttribute("errors", formErrors);

            if (formErrors.isContainsErrors()) {
                return Links.PASSWORD_EDIT_PAGE;
            }
            request.removeAttribute("errors");
            return Links.EDIT_PERSONAL_DATA_SUCCESS_PAGE;
        }
    }
}
