package ua.org.training.library.dto.builders;

import ua.org.training.library.dto.UserManagementDTO;
import ua.org.training.library.model.Role;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public class UserManagementDTOBuilder {
    private long id;
    private String login;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private boolean enabled;
    private Date dateCreated;
    private Date dateUpdated;
    private List<String> roles;

    public UserManagementDTOBuilder setId(long id) {
        this.id = id;
        return this;
    }

    public UserManagementDTOBuilder setLogin(String login) {
        this.login = login;
        return this;
    }

    public UserManagementDTOBuilder setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserManagementDTOBuilder setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserManagementDTOBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserManagementDTOBuilder setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public UserManagementDTOBuilder setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public UserManagementDTOBuilder setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public UserManagementDTOBuilder setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
        return this;
    }

    public UserManagementDTOBuilder setRoles(List<String> roles) {
        this.roles = roles;
        return this;
    }

    public UserManagementDTO createUserManagementDTO() {
        return new UserManagementDTO(id, login, firstName, lastName, email, phone, enabled, dateCreated, dateUpdated, roles);
    }
}