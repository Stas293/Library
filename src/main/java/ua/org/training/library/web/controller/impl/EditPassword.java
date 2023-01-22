package ua.org.training.library.web.controller.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;
import ua.org.training.library.context.ApplicationContext;
import ua.org.training.library.exceptions.ConnectionDBException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.form.FormValidationError;
import ua.org.training.library.security.AuthorityUser;
import ua.org.training.library.security.SecurityService;
import ua.org.training.library.service.UserService;
import ua.org.training.library.utility.validation.UserValidation;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.Links;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.web.controller.ControllerCommand;

public class EditPassword implements ControllerCommand {
    private static final Logger LOGGER = LogManager.getLogger(EditPassword.class);
    private final UserService userService = ApplicationContext.getInstance().getUserService();
    private final UserValidation userValidation = ApplicationContext.getInstance().getUserValidation();
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        if (request.getMethod().equals("GET")) {
            LOGGER.debug("Form was sent by GET method");
            if (SecurityService.getAuthorityUser(request)== AuthorityUser.ANONYMOUS) {
                LOGGER.debug("User is not authorized");
                return Links.LOGIN_PAGE;
            }
            return Links.PASSWORD_EDIT_PAGE;
        } else {

            FormValidationError formErrors = new FormValidationError();

            LOGGER.debug(String.format("Form was sent by POST method. Parameters: %s",
                    Utility.getParameters(request)));

            String password = Utility.getStringParameter(
                    request.getParameter(
                            Constants.RequestAttributes.APP_PASSWORD_ATTRIBUTE),
                    Constants.APP_STRING_DEFAULT_VALUE);
            String confirmPassword = Utility.getStringParameter(
                    request.getParameter(
                            Constants.RequestAttributes.ACCOUNT_CONFIRM_PASSWORD_ATTRIBUTE),
                    Constants.APP_STRING_DEFAULT_VALUE);
            LOGGER.debug(String.format("User password: %s, confirm password: %s", password, confirmPassword));

            validatePassword(formErrors, password, confirmPassword);

            LOGGER.debug(String.format("Form errors: %s", formErrors));

            if (!formErrors.isContainsErrors()) {
                String passwordEditPageFail = updateUserPassword(request, password);
                if (passwordEditPageFail != null) return passwordEditPageFail;
            }
            request.setAttribute("errors", formErrors);

            if (formErrors.isContainsErrors()) {
                return Links.PASSWORD_EDIT_PAGE;
            }
            request.removeAttribute("errors");
            return Links.EDIT_PERSONAL_DATA_SUCCESS_PAGE;
        }
    }

    private void validatePassword(FormValidationError formErrors, String password, String confirmPassword) {
        userValidation.validatePasswordLength(password, formErrors);
        userValidation.validatePasswordLength(confirmPassword, formErrors);
        userValidation.validateConfirmPassword(
                password,
                confirmPassword,
                formErrors);
    }

    private String updateUserPassword(HttpServletRequest request, String password) {
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
        return null;
    }
}
