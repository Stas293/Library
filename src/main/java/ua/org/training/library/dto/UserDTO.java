package ua.org.training.library.dto;

import ua.org.training.library.dto.builders.UserDTOBuilder;

import java.io.Serializable;

public class UserDTO implements Serializable {
    private String login;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    public UserDTO() {
    }

    public UserDTO(String login, String firstName, String lastName, String email, String phone) {
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public static UserDTOBuilder builder() {
        return new UserDTOBuilder();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "login='" + login + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDTO userDTO)) return false;

        if (!login.equals(userDTO.login)) return false;
        if (!firstName.equals(userDTO.firstName)) return false;
        if (!lastName.equals(userDTO.lastName)) return false;
        if (!email.equals(userDTO.email)) return false;
        return phone.equals(userDTO.phone);
    }

    @Override
    public int hashCode() {
        int result = login.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + phone.hashCode();
        return result;
    }
}
