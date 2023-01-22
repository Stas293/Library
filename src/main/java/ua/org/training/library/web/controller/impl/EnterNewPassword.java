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
import ua.org.training.library.form.ResetValidationError;
import ua.org.training.library.service.UserService;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.Links;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.utility.validation.ResetValidation;
import ua.org.training.library.web.controller.ControllerCommand;

public class EnterNewPassword implements ControllerCommand {
    private static final Logger LOGGER = LogManager.getLogger(EnterNewPassword.class);
    private static final String CODE = "code";
    private static final String EMAIL = "email";
    private final UserService userService = ApplicationContext.getInstance().getUserService();
    private final ResetValidation resetValidation = ApplicationContext.getInstance().getResetValidation();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        if (request.getMethod().equals("GET")) {
            if (session.getAttribute(CODE) == null
                    || session.getAttribute(EMAIL) == null
                    || !request.getParameter(CODE).equals(session.getAttribute(CODE))
                    || !request.getParameter(EMAIL).equals(session.getAttribute(EMAIL))) {
                return Links.RESET_PASSWORD;
            } else {
                return Links.ENTER_NEW_PASSWORD;
            }
        }
        String password = Utility.getStringParameter(
                request.getParameter("password"),
                Constants.APP_STRING_DEFAULT_VALUE);
        String confirmPassword = Utility.getStringParameter(
                request.getParameter("confirmPassword"),
                Constants.APP_STRING_DEFAULT_VALUE);
        String enterNewPassword = validateNewPassword(request, password, confirmPassword);
        if (enterNewPassword != null) return enterNewPassword;
        String enterNewPassword1 = setNewPassword(session, password);
        if (enterNewPassword1 != null) return enterNewPassword1;
        session.removeAttribute(CODE);
        session.removeAttribute(EMAIL);
        return Links.LOGIN_PAGE;
    }

    private String setNewPassword(HttpSession session, String password) {
        try {
            String bcryptPassword = BCrypt.hashpw(
                    password, BCrypt.gensalt(Constants.APP_BCRYPT_SALT));
            userService.updateUserPassword(
                    userService.getUserByEmail(session.getAttribute(EMAIL).toString()),
                    bcryptPassword);
        } catch (ServiceException e) {
            LOGGER.error("Error while updating user password", e);
            return Links.ENTER_NEW_PASSWORD;
        } catch (ConnectionDBException e) {
            LOGGER.error("Error while connecting to database", e);
            return Links.ENTER_NEW_PASSWORD;
        }
        return null;
    }

    private String validateNewPassword(HttpServletRequest request, String password, String confirmPassword) {
        ResetValidationError resetValidationError = new ResetValidationError();
        resetValidation.validateConfirmPassword(password, confirmPassword, resetValidationError);
        resetValidation.validatePasswordLength(password, resetValidationError);
        if (resetValidationError.isContainsErrors()) {
            request.setAttribute("resetValidationError", resetValidationError);
            return Links.ENTER_NEW_PASSWORD;
        }
        return null;
    }
}
