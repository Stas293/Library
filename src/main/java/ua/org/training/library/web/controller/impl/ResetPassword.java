package ua.org.training.library.web.controller.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.context.ApplicationContext;
import ua.org.training.library.exceptions.UnexpectedValidationException;
import ua.org.training.library.form.ResetValidationError;
import ua.org.training.library.service.MailService;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.Links;
import ua.org.training.library.utility.MailSender;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.utility.validation.ResetValidation;
import ua.org.training.library.web.controller.ControllerCommand;

import java.util.ResourceBundle;

public class ResetPassword implements ControllerCommand {
    private static final Logger LOGGER = LogManager.getLogger(ResetPassword.class);
    private final MailService mailService = ApplicationContext.getInstance().getMailService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        if (request.getMethod().equals("GET")) {
            return Links.RESET_PASSWORD;
        }
        String email = Utility.getStringParameter(
                request.getParameter(Constants.RequestAttributes.ACCOUNT_EMAIL_ATTRIBUTE),
                Constants.APP_STRING_DEFAULT_VALUE);
        ResetValidationError resetValidationError = new ResetValidationError();
        ResetValidation resetValidation = ApplicationContext.getInstance().getResetValidation();
        try {
            resetValidation.checkIfUserEmailExists(email, resetValidationError);
        } catch (UnexpectedValidationException e) {
            LOGGER.error("Error while checking if user email exists", e);
            return Links.RESET_PASSWORD;
        }
        if (resetValidationError.isContainsErrors()) {
            request.setAttribute("resetValidationError", resetValidationError);
            return Links.RESET_PASSWORD;
        }
        String code = mailService.sendResetPasswordMail(Utility.getLocale(request), email);
        request.getSession().setAttribute("code", code);
        request.getSession().setAttribute("email", request.getParameter("email"));
        return Links.RESET_PASSWORD;
    }
}
