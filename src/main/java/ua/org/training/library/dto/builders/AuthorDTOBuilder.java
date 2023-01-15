package ua.org.training.library.dto.builders;

import ua.org.training.library.dto.AuthorDTO;

public class AuthorDTOBuilder {
    private long id;
    private String firstName;
    private String lastName;

    public AuthorDTOBuilder setId(long id) {
        this.id = id;
        return this;
    }

    public AuthorDTOBuilder setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public AuthorDTOBuilder setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public AuthorDTO createAuthorDTO() {
        return new AuthorDTO(id, firstName, lastName);
    }
}
