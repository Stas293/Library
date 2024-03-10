package ua.org.training.library.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class User {
    private Long id;
    private String login;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private boolean enabled;
    private Collection<Role> roles = List.of();

    public User(String login) {
        this.login = login;
    }

    public User(String login, String firstName, String lastName, Collection<Role> roles) {
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }
}
