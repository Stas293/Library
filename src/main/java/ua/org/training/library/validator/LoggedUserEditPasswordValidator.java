package ua.org.training.library.validator;

import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.dto.UserLoggedUpdatePasswordDto;
import ua.org.training.library.enums.Patterns;
import ua.org.training.library.enums.Validation;
import ua.org.training.library.form.LoggedUserUpdatePasswordFormValidationError;

import java.util.regex.Pattern;

@Slf4j
@Component
public class LoggedUserEditPasswordValidator {
    public LoggedUserUpdatePasswordFormValidationError validate(UserLoggedUpdatePasswordDto user) {
        log.info("Validating user password update");
        log.info("User: {}", user);
        LoggedUserUpdatePasswordFormValidationError errors = new LoggedUserUpdatePasswordFormValidationError();

        String password = user.getPassword();
        String confirmPassword = user.getConfirmPassword();

        validatePasswordEquals(
                password,
                confirmPassword,
                errors);

        validatePasswordPattern(
                password,
                Patterns.PASSWORD_PATTERN.getPattern(),
                errors);

        log.info("Errors: {}", errors);
        return errors;
    }

    private void validatePasswordPattern(String password, String pattern, LoggedUserUpdatePasswordFormValidationError errors) {
        Pattern p = Pattern.compile(pattern);
        if (!p.matcher(password).matches()) {
            errors.setPassword(Validation.PASSWORD_PATTERN_ERROR.getMessage());
        }
    }


    private void validatePasswordEquals(String password, String confirmPassword, LoggedUserUpdatePasswordFormValidationError errors) {
        if (!password.equals(confirmPassword)) {
            errors.setConfirmPassword(Validation.PASSWORD_NOT_MATCH_ERROR.getMessage());
        }
    }
}
