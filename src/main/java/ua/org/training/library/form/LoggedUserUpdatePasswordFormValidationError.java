package ua.org.training.library.form;

import lombok.Data;

@Data
public class LoggedUserUpdatePasswordFormValidationError {
    private String oldPassword;
    private String password;
    private String confirmPassword;
    private boolean containsErrors = false;

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
        this.containsErrors = true;
    }

    public void setPassword(String password) {
        this.password = password;
        this.containsErrors = true;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        this.containsErrors = true;
    }
}
