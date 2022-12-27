package ua.org.training.library.model.builders;

import ua.org.training.library.model.Author;

public class AuthorBuilder {
    private Long id;
    private String firstName;
    private String lastName;

    public AuthorBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public AuthorBuilder setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public AuthorBuilder setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Author createAuthor() {
        return new Author(id, firstName, lastName);
    }
}