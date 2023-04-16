package ua.org.training.library.validator;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.enums.Patterns;
import ua.org.training.library.enums.Validation;
import ua.org.training.library.form.PersonalEditFormValidationError;
import ua.org.training.library.model.User;

import java.util.regex.Pattern;

@Component
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserEditPersonalValidator {

    public PersonalEditFormValidationError validation(User user) {
        log.info("Validation user");
        log.info("User: {}", user);
        PersonalEditFormValidationError errors = new PersonalEditFormValidationError();
        validateFirstNamePattern(
                user.getFirstName(),
                Patterns.NAME_PATTERN.getPattern(),
                errors);

        validateLastNamePattern(
                user.getLastName(),
                Patterns.NAME_PATTERN.getPattern(),
                errors);

        validateEmailPattern(
                user.getEmail(),
                Patterns.EMAIL_PATTERN.getPattern(),
                errors);

        validatePhonePattern(
                user.getPhone(),
                Patterns.PHONE_PATTERN.getPattern(),
                errors);
        log.info("Errors: {}", errors);

        return errors;
    }

    private void validateFirstNamePattern(String firstName, String pattern, PersonalEditFormValidationError errors) {
        if (!Pattern.matches(pattern, firstName)) {
            errors.setFirstName(Validation.NAME_PATTERN_ERROR.getMessage());
        }
    }

    private void validateLastNamePattern(String lastName, String pattern, PersonalEditFormValidationError errors) {
        if (!Pattern.matches(pattern, lastName)) {
            errors.setLastName(Validation.NAME_PATTERN_ERROR.getMessage());
        }
    }

    private void validateEmailPattern(String email, String pattern, PersonalEditFormValidationError errors) {
        if (!Pattern.matches(pattern, email)) {
            errors.setEmail(Validation.EMAIL_PATTERN_ERROR.getMessage());
        }
    }

    private void validatePhonePattern(String phone, String pattern, PersonalEditFormValidationError errors) {
        if (!Pattern.matches(pattern, phone)) {
            errors.setPhone(Validation.PHONE_PATTERN_ERROR.getMessage());
        }
    }
}
