package ua.org.training.library.dto;

import ua.org.training.library.dto.builders.AuthorDTOBuilder;
import ua.org.training.library.model.Author;

import java.io.Serializable;

public class AuthorDTO implements Serializable {
    private long id;
    private String firstName;
    private String lastName;

    public AuthorDTO(long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static AuthorDTOBuilder builder() {
        return new AuthorDTOBuilder();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "AuthorDTO{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthorDTO authorDTO)) return false;

        if (!firstName.equals(authorDTO.firstName)) return false;
        return lastName.equals(authorDTO.lastName);
    }

    @Override
    public int hashCode() {
        int result = firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        return result;
    }
}
