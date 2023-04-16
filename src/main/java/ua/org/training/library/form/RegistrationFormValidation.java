package ua.org.training.library.form;

public class RegistrationFormValidation {
    private String login;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String captcha;
    private boolean containsErrors = false;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
        this.containsErrors = true;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        this.containsErrors = true;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        this.containsErrors = true;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        this.containsErrors = true;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        this.containsErrors = true;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        this.containsErrors = true;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captchaError) {
        this.captcha = captchaError;
        this.containsErrors = true;
    }

    public boolean containsErrors() {
        return containsErrors;
    }

    public void setContainsErrors(boolean containsErrors) {
        this.containsErrors = containsErrors;
    }
}
