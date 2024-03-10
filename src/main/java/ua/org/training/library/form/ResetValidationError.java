package ua.org.training.library.form;

import lombok.Data;

@Data
public class ResetValidationError {
    private String code;
    private String password;
    private String confirmPassword;
    private boolean containsErrors = false;

    public void setCode(String code) {
        this.code = code;
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
