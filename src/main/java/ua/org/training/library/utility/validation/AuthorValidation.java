package ua.org.training.library.utility.validation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.form.AuthorValidationError;
import ua.org.training.library.model.Author;
import ua.org.training.library.utility.Constants;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AuthorValidation {
    private static final Logger LOGGER = LogManager.getLogger(AuthorValidation.class);
    private static final int MIN_THREE = 3;
    private static final int MAX_255 = 255;

    public void validation(Locale locale, Author author, AuthorValidationError errors) {
        ResourceBundle bundle = ResourceBundle.getBundle(
                Constants.BUNDLE_NAME,
                locale);
        validateFirstNameLength(author.getFirstName(), errors);
        validateLastNameLength(author.getLastName(), errors);

        String pattenFirstName = locale.getLanguage().equals(Constants.APP_DEFAULT_LANGUAGE) ?
                bundle.getString(Constants.BundleStrings.APP_FIRST_NAME_PATTERN) :
                bundle.getString(Constants.BundleStrings.APP_FIRST_NAME_PATTERN_UKR);

        String pattenLastName = locale.getLanguage().equals(Constants.APP_DEFAULT_LANGUAGE) ?
                bundle.getString(Constants.BundleStrings.APP_LAST_NAME_PATTERN) :
                bundle.getString(Constants.BundleStrings.APP_LAST_NAME_PATTERN_UKR);

        validateFirstNamePattern(author.getFirstName(), pattenFirstName, errors);
        validateLastNamePattern(author.getLastName(), pattenLastName, errors);
    }

    private void validateFirstNameLength(String firstName, AuthorValidationError errors) {
        if (firstName.length() < MIN_THREE || firstName.length() > MAX_255) {
            LOGGER.debug("First name length is not valid length: {}", firstName.length());
            errors.setFirstName(Constants.Validation.NAME_LENGTH);
        }
    }

    private void validateLastNameLength(String lastName, AuthorValidationError errors) {
        if (lastName.length() < MIN_THREE || lastName.length() > MAX_255) {
            LOGGER.debug("Last name length is not valid length: {}", lastName.length());
            errors.setLastName(Constants.Validation.NAME_LENGTH);
        }
    }

    private void validateFirstNamePattern(String firstName, String pattenFirstName, AuthorValidationError errors) {
        Pattern r = Pattern.compile(pattenFirstName);
        if (!r.matcher(firstName).matches()) {
            LOGGER.debug("First name is not valid: {}", firstName);
            errors.setFirstName(Constants.Validation.NAME_PATTERN_ERROR);
        }
    }

    private void validateLastNamePattern(String lastName, String pattenLastName, AuthorValidationError errors) {
        Pattern r = Pattern.compile(pattenLastName);
        if (!r.matcher(lastName).matches()) {
            LOGGER.debug("Last name is not valid: {}", lastName);
            errors.setLastName(Constants.Validation.NAME_PATTERN_ERROR);
        }
    }
}
