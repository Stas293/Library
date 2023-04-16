package ua.org.training.library.form;

public class ResetValidationError {
    private String code;
    private String password;
    private String confirmPassword;
    private boolean containsErrors = false;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
        this.containsErrors = true;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        this.containsErrors = true;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        this.containsErrors = true;
    }

    public boolean containsErrors() {
        return containsErrors;
    }

    public void setContainsErrors(boolean containsErrors) {
        this.containsErrors = containsErrors;
    }
}
