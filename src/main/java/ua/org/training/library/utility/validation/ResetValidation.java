package ua.org.training.library.utility.validation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.exceptions.ConnectionDBException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.exceptions.UnexpectedValidationException;
import ua.org.training.library.form.AuthorValidationError;
import ua.org.training.library.form.FormValidationError;
import ua.org.training.library.form.ResetValidationError;
import ua.org.training.library.model.Author;
import ua.org.training.library.model.User;
import ua.org.training.library.service.UserService;
import ua.org.training.library.utility.Constants;

import java.util.Locale;

public class ResetValidation {
    private static final Logger LOGGER = LogManager.getLogger(ResetValidation.class);
    private final UserService userService;

    public ResetValidation(UserService userService) {
        this.userService = userService;
    }

    public void checkIfUserEmailExists(String email, ResetValidationError errors) throws UnexpectedValidationException {
        try {
            userService.getUserByEmail(email);
        } catch (ServiceException e) {
            LOGGER.error(String.format("Error while getting user by email: %s", email), e);
            errors.setEmail(Constants.Validation.USER_EMAIL_NOT_EXISTS);
        } catch (ConnectionDBException e) {
            LOGGER.error(String.format("Error while getting user by email: %s", email), e);
            throw new UnexpectedValidationException(e.getMessage(), e);
        }
    }

    public void validatePasswordLength(String password, ResetValidationError errors) {
        if (notValidFieldLength(password, Constants.MIN_EIGHT, Constants.MAX_255))
            errors.setPassword(Constants.Validation.PASSWORD_LENGTH);
    }

    public void validateConfirmPassword(String password, String confirmPassword, ResetValidationError errors) {
        if (!password.equals(confirmPassword))
            errors.setPassword(Constants.Validation.PASSWORD_ERROR);
    }

    private boolean notValidFieldLength(String field, int min, int max) {
        return field.length() < min || field.length() > max;
    }
}
