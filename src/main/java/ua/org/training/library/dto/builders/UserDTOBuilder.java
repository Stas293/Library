package ua.org.training.library.dto.builders;

import ua.org.training.library.dto.UserDTO;
import ua.org.training.library.model.User;

public class UserDTOBuilder {
    private String login;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    public UserDTOBuilder setLogin(String login) {
        this.login = login;
        return this;
    }

    public UserDTOBuilder setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserDTOBuilder setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserDTOBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserDTOBuilder setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public UserDTO createUserDTO() {
        return new UserDTO(login, firstName, lastName, email, phone);
    }
}