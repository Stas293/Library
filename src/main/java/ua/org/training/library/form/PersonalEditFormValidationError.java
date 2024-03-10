package ua.org.training.library.form;

import lombok.Data;

@Data
public class PersonalEditFormValidationError {
    private String login;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private boolean containsErrors = false;

    public void setLogin(String login) {
        this.login = login;
        this.containsErrors = true;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        this.containsErrors = true;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        this.containsErrors = true;
    }

    public void setEmail(String email) {
        this.email = email;
        this.containsErrors = true;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        this.containsErrors = true;
    }
}
