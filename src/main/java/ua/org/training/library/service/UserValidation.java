package ua.org.training.library.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.model.User;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.form.FormValidationError;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class UserValidation {
    private static final Logger LOGGER = LogManager.getLogger(UserValidation.class);
    private final UserService userService;

    public UserValidation(UserService userService) {
        this.userService = userService;
    }

    private static final int MIN_THREE = 3;
    private static final int MIN_SIX = 6;
    private static final int MIN_EIGHT = 8;
    private static final int MAX_255 = 255;

    public void validation(Locale locale, User user, FormValidationError errors) {
        ResourceBundle bundle = ResourceBundle.getBundle(
                Constants.BUNDLE_NAME,
                locale);

        validateLoginLength(user.getLogin(), errors);
        validateLoginPattern(
                user.getLogin(),
                bundle.getString(Constants.BundleStrings.APP_LOGIN_PATTERN),
                errors);
        validateLogin(user.getLogin(), errors);

        validateFirstNameLength(user.getFirstName(), errors);
        String pattenFirstName = locale.getLanguage().equals(Constants.APP_DEFAULT_LANGUAGE) ?
                bundle.getString(Constants.BundleStrings.APP_FIRST_NAME_PATTERN) :
                bundle.getString(Constants.BundleStrings.APP_FIRST_NAME_PATTERN_UKR);
        validateFirstNamePattern(
                user.getFirstName(),
                pattenFirstName,
                errors);

        validateLastNameLength(user.getLastName(), errors);
        String pattenLastName = locale.getLanguage().equals(Constants.APP_DEFAULT_LANGUAGE) ?
                bundle.getString(Constants.BundleStrings.APP_LAST_NAME_PATTERN) :
                bundle.getString(Constants.BundleStrings.APP_LAST_NAME_PATTERN_UKR);
        validateLastNamePattern(
                user.getLastName(),
                pattenLastName,
                errors);

        validateEmailPattern(
                user.getEmail(),
                bundle.getString(Constants.BundleStrings.APP_EMAIL_PATTERN),
                errors);
        validateEmailLength(user.getEmail(), errors);
        validateEmail(user.getEmail(), errors);

        validatePhonePattern(
                user.getPhone(),
                bundle.getString(Constants.BundleStrings.APP_PHONE_PATTERN),
                errors);
        validatePhone(user.getPhone(), errors);
    }

    private void validateLastNamePattern(String lastName, String pattern, FormValidationError errors) {
        Pattern r = Pattern.compile(pattern);
        if (!r.matcher(lastName).matches())
            errors.setLastName(Constants.Validation.NAME_PATTERN_ERROR);
    }

    private void validateFirstNamePattern(String firstName, String pattern, FormValidationError errors) {
        Pattern r = Pattern.compile(pattern);
        if (!r.matcher(firstName).matches())
            errors.setFirstName(Constants.Validation.NAME_PATTERN_ERROR);
    }

    private void validateFirstNameLength(String firstName, FormValidationError errors) {
        if (notValidFieldLength(firstName, MIN_THREE, MAX_255))
            errors.setFirstName(Constants.Validation.NAME_LENGTH);
    }

    private void validateLastNameLength(String lastName, FormValidationError errors) {
        if (notValidFieldLength(lastName, MIN_THREE, MAX_255))
            errors.setLastName(Constants.Validation.NAME_LENGTH);
    }

    public void validateConfirmPassword(String password, String confirmPassword, FormValidationError errors) {
        if (!password.equals(confirmPassword))
            errors.setPassword(Constants.Validation.PASSWORD_ERROR);
    }

    private void validateLoginPattern(String login, String pattern, FormValidationError errors) {
        Pattern r = Pattern.compile(pattern);
        if (!r.matcher(login).matches())
            errors.setLogin(Constants.Validation.LOGIN_PATTERN_ERROR);
    }

    private void validateEmailPattern(String email, String pattern, FormValidationError errors) {
        Pattern r = Pattern.compile(pattern);
        LOGGER.info(email + " : " + pattern);
        if (!r.matcher(email).matches())
            errors.setEmail(Constants.Validation.EMAIL_PATTERN_ERROR);
    }

    private void validatePhonePattern(String phone, String pattern, FormValidationError errors) {
        Pattern r = Pattern.compile(pattern);
        if (!r.matcher(phone).matches())
            errors.setPhone(Constants.Validation.PHONE_PATTERN_ERROR);
    }

    private void validateLoginLength(String username, FormValidationError errors) {
        if (notValidFieldLength(username, MIN_SIX, MAX_255))
            errors.setLogin(Constants.Validation.EMAIL_LOGIN_LENGTH);
    }

    private void validateEmailLength(String email, FormValidationError errors) {
        if (notValidFieldLength(email, MIN_SIX, MAX_255))
            errors.setEmail(Constants.Validation.EMAIL_LOGIN_LENGTH);
    }

    public void validatePasswordLength(String password, FormValidationError errors) {
        if (notValidFieldLength(password, MIN_EIGHT, MAX_255))
            errors.setPassword(Constants.Validation.PASSWORD_LENGTH);
    }

    private boolean notValidFieldLength(String field, int min, int max) {
        return field.length() < min || field.length() > max;
    }

    private void validateLogin(String login, FormValidationError errors) {
        try {
            userService.getUserByLogin(login);
        } catch (ServiceException e) {
            LOGGER.error(e.getMessage());
            return;
        }
        errors.setLogin(Constants.Validation.DUPLICATE_FIELD);
        LOGGER.info("Validation failed: duplicate login -> " + login);
    }

    private void validateEmail(String email, FormValidationError errors) {
        try {
            userService.getUserByEmail(email);
        } catch (ServiceException e) {
            LOGGER.error("error: " + e.getMessage());
            return;
        }
        LOGGER.debug("Validation failed: duplicate email -> " + email);
        errors.setEmail(Constants.Validation.DUPLICATE_FIELD);
    }

    private void validatePhone(String phone, FormValidationError errors) {
        try {
            userService.getUserByPhone(phone);
        } catch (ServiceException e) {
            LOGGER.error("error: " + e.getMessage());
            return;
        }
        LOGGER.debug("Validation failed: duplicate phone -> " + phone);
        errors.setPhone(Constants.Validation.DUPLICATE_FIELD);
    }
}
