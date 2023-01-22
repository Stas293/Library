package ua.org.training.library.web.controller.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;
import ua.org.training.library.context.ApplicationContext;
import ua.org.training.library.exceptions.ConnectionDBException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.exceptions.UnexpectedValidationException;
import ua.org.training.library.model.User;
import ua.org.training.library.security.AuthorityUser;
import ua.org.training.library.security.SecurityService;
import ua.org.training.library.service.RoleService;
import ua.org.training.library.service.UserService;
import ua.org.training.library.utility.validation.UserValidation;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.Links;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.web.controller.ControllerCommand;
import ua.org.training.library.form.FormValidationError;

import java.util.Collections;

public class Register implements ControllerCommand {
    private static final Logger LOGGER = LogManager.getLogger(Register.class);

    private final UserService userService = ApplicationContext.getInstance().getUserService();
    private final RoleService roleService = ApplicationContext.getInstance().getRoleService();
    private final UserValidation userValidation = ApplicationContext.getInstance().getUserValidation();

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response) {

        AuthorityUser securityAccount = SecurityService.getAuthorityUser(request);

        if (!securityAccount.getLogin().equals(Constants.APP_UNAUTHORIZED_USER)) {
            SecurityService.removeLoggedUserFromSession(request, securityAccount.getLogin());
            return Links.REGISTRATION_PAGE;
        }

        if (request.getMethod().equals("GET")) {
            LOGGER.debug("Form was sent by GET method");
            return Links.REGISTRATION_PAGE;
        } else {

            FormValidationError formErrors = new FormValidationError();

            LOGGER.debug("FormValidator: " + formErrors);

            User user = userService.collectUserFromRequest(request);
            String password = Utility.getStringParameter(
                    request.getParameter(
                            Constants.RequestAttributes.APP_PASSWORD_ATTRIBUTE),
                    Constants.APP_STRING_DEFAULT_VALUE);
            String confirmPassword = Utility.getStringParameter(
                    request.getParameter(
                            Constants.RequestAttributes.ACCOUNT_CONFIRM_PASSWORD_ATTRIBUTE),
                    Constants.APP_STRING_DEFAULT_VALUE);
            String captchaResponse = Utility.getStringParameter(
                    request.getParameter(
                            Constants.RequestAttributes.APP_CAPTCHA_RESPONSE_ATTRIBUTE),
                    Constants.APP_STRING_DEFAULT_VALUE);
            LOGGER.debug(String.format("Account: %s, password: %s, confirmPassword: %s",
                    user, password, confirmPassword));
            try {
                user.setRoles(Collections.singletonList(roleService.getRoleByCode(Constants.DEFAULT_USER_ROLE)));
            } catch (ServiceException | ConnectionDBException e) {
                LOGGER.error("Error while getting role by code", e);
                return Links.REGISTRATION_PAGE;
            }

            String errorPage = validateUserData(request, formErrors, user, password, confirmPassword, captchaResponse);
            if (errorPage != null) return errorPage;

            if (!formErrors.isContainsErrors()) {
                String registrationPage = registerUser(user, password);
                if (registrationPage != null) return registrationPage;
            }
            request.setAttribute("user_reg", user);
            request.setAttribute("errors", formErrors);

            if (formErrors.isContainsErrors()) {
                return Links.REGISTRATION_PAGE;
            }
            return Links.REGISTRATION_FORM_SUCCESS;
        }
    }

    private String validateUserData(HttpServletRequest request, FormValidationError formErrors, User user, String password, String confirmPassword, String captchaResponse) {
        userValidation.validatePasswordLength(password, formErrors);
        userValidation.validatePasswordLength(confirmPassword, formErrors);
        userValidation.validateConfirmPassword(
                password,
                confirmPassword,
                formErrors);

        userValidation.validateCaptcha(captchaResponse, formErrors);

        LOGGER.debug(String.format("FormValidator: %s", formErrors));

        try {
            userValidation.validation(Utility.getLocale(request),
                    user,
                    formErrors);
        } catch (UnexpectedValidationException e) {
            LOGGER.error("Unexpected validation error", e);
            return Links.ERROR_PAGE + "?message=" + e.getMessage();
        }
        LOGGER.debug(String.format("FormValidator: %s", formErrors));
        return null;
    }

    private String registerUser(User user, String password) {
        try {
            String bcryptPassword = BCrypt.hashpw(
                    password, BCrypt.gensalt(Constants.APP_BCRYPT_SALT));
            userService.createUser(user, bcryptPassword);
        } catch (ServiceException e) {
            LOGGER.error("Error while creating user", e);
            return Links.REGISTRATION_PAGE;
        } catch (ConnectionDBException e) {
            LOGGER.error("Error while creating user", e);
            return Links.ERROR_PAGE + "?message=" + e.getMessage();
        }
        return null;
    }
}
