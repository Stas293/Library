package ua.org.training.library.web.controller.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;
import ua.org.training.library.exceptions.ControllerException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.model.User;
import ua.org.training.library.security.AuthorityUser;
import ua.org.training.library.security.SecurityService;
import ua.org.training.library.service.RoleService;
import ua.org.training.library.service.UserService;
import ua.org.training.library.service.UserValidation;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.Links;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.web.controller.ControllerCommand;
import ua.org.training.library.form.FormValidationError;

import java.util.Collections;

public class Register implements ControllerCommand {
    private static final Logger LOGGER = LogManager.getLogger(Register.class);

    private final UserService userService = new UserService();
    private final RoleService roleService = new RoleService();
    private final UserValidation userValidation = new UserValidation(userService);

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response) {

        HttpSession session = request.getSession();

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
            LOGGER.debug(String.format("Account: %s, password: %s, confirmPassword: %s",
                    user, password, confirmPassword));
            try {
                user.setRoles(Collections.singletonList(roleService.getRoleByCode(Constants.DEFAULT_USER_ROLE)));
            } catch (ServiceException e) {
                LOGGER.error("Error while getting role by code", e);
                throw new ControllerException("Error while getting role by code", e);
            }

            userValidation.validatePasswordLength(password, formErrors);
            userValidation.validatePasswordLength(password, formErrors);
            userValidation.validateConfirmPassword(
                    password,
                    confirmPassword,
                    formErrors);

            LOGGER.debug("error has formErrors = " + formErrors.isContainsErrors());

            userValidation.validation(Utility.getLocale(request),
                    user,
                    formErrors);
            LOGGER.debug("error has formErrors = " + formErrors.isContainsErrors());

            if (!formErrors.isContainsErrors())
                try {
                    String bcryptPassword = BCrypt.hashpw(
                            password, BCrypt.gensalt(Constants.APP_BCRYPT_SALT));
                    userService.createUser(user, bcryptPassword);
                } catch (ServiceException e) {
                    LOGGER.error("Error while creating user", e);
                    throw new ControllerException("Error while creating user", e);
                }
            session.setAttribute("user_reg", user);
            session.setAttribute("errors", formErrors);

            if (formErrors.isContainsErrors()) {
                return Links.REGISTRATION_PAGE;
            }
            session.removeAttribute("user_reg");
            session.removeAttribute("errors");
            clearRequestSessionAttributes(request);
            return Links.REGISTRATION_FORM_SUCCESS;
        }
    }

    @Override
    public void clearRequestSessionAttributes(HttpServletRequest request) {
        request.getSession().removeAttribute(Constants.RequestAttributes.ACCOUNT_CONFIRM_PASSWORD_ATTRIBUTE);
        request.getSession().removeAttribute(Constants.RequestAttributes.ACCOUNT_EMAIL_ATTRIBUTE);
        request.getSession().removeAttribute(Constants.RequestAttributes.ACCOUNT_FIRST_NAME_ATTRIBUTE);
        request.getSession().removeAttribute(Constants.RequestAttributes.ACCOUNT_LAST_NAME_ATTRIBUTE);
        request.getSession().removeAttribute(Constants.RequestAttributes.ACCOUNT_PHONE_ATTRIBUTE);
        request.getSession().removeAttribute(Constants.RequestAttributes.APP_PASSWORD_ATTRIBUTE);
        request.getSession().removeAttribute(Constants.RequestAttributes.APP_LOGIN_ATTRIBUTE);
    }
}
