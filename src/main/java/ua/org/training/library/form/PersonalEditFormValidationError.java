package ua.org.training.library.form;

public class PersonalEditFormValidationError {
    private String login;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
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

    public boolean isContainsErrors() {
        return containsErrors;
    }

    public void setContainsErrors(boolean containsErrors) {
        this.containsErrors = containsErrors;
    }
}
