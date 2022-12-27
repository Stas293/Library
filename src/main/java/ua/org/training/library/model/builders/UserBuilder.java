package ua.org.training.library.model.builders;

import ua.org.training.library.model.Role;
import ua.org.training.library.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;

public class UserBuilder {
    private Long id;
    private String login;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private boolean enabled;
    private Date dateCreated;
    private Date dateUpdated;
    private Collection<Role> roles;

    public UserBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public UserBuilder setLogin(String login) {
        this.login = login;
        return this;
    }

    public UserBuilder setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserBuilder setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public UserBuilder setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public UserBuilder setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public UserBuilder setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
        return this;
    }

    public UserBuilder setRoles(Collection<Role> roles) {
        this.roles = roles;
        return this;
    }

    public User createUser() {
        return new User(id, login, firstName, lastName, email, phone, enabled, dateCreated, dateUpdated, roles);
    }
}