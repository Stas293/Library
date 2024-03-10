package ua.org.training.library.form;

import lombok.Data;

@Data
public class AuthorSaveFormValidationError {
    private String firstName;
    private String middleName;
    private String lastName;
    private boolean containsErrors = false;

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        this.containsErrors = true;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
        this.containsErrors = true;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        this.containsErrors = true;
    }
}
