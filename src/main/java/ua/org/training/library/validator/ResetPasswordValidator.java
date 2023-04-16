package ua.org.training.library.validator;


import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.dto.UserChangePasswordDto;
import ua.org.training.library.enums.Patterns;
import ua.org.training.library.enums.Validation;
import ua.org.training.library.form.ResetValidationError;

import java.util.regex.Pattern;

@Slf4j
@Component
public class ResetPasswordValidator {
    public ResetValidationError validate(UserChangePasswordDto userChangePasswordDto) {
        ResetValidationError error = new ResetValidationError();
        String password = userChangePasswordDto.getNewPassword();
        String passwordConfirm = userChangePasswordDto.getNewPasswordConfirm();
        if (password == null || password.isEmpty()) {
            error.setPassword(Validation.EMPTY_PASSWORD.getMessage());
        }
        if (passwordConfirm == null || passwordConfirm.isEmpty()) {
            error.setConfirmPassword(Validation.EMPTY_PASSWORD.getMessage());
        }
        validatePasswordPattern(
                password,
                Patterns.PASSWORD_PATTERN.getPattern(),
                error);
        return error;
    }

    private void validatePasswordPattern(String password, String pattern, ResetValidationError error) {
        Pattern passwordPattern = Pattern.compile(pattern);
        if (!passwordPattern.matcher(password).matches()) {
            error.setPassword(Validation.PASSWORD_PATTERN_ERROR.getMessage());
        }
    }
}
