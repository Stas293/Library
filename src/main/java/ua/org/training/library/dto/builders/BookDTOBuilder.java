package ua.org.training.library.dto.builders;

import ua.org.training.library.dto.AuthorDTO;
import ua.org.training.library.dto.BookDTO;

import java.util.List;

public class BookDTOBuilder {
    private long id;
    private String name;
    private int count;
    private String ISBN;
    private String publicationDate;
    private String fine;
    private String language;
    private List<AuthorDTO> authors;

    public BookDTOBuilder setId(long id) {
        this.id = id;
        return this;
    }

    public BookDTOBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public BookDTOBuilder setCount(int count) {
        this.count = count;
        return this;
    }

    public BookDTOBuilder setISBN(String ISBN) {
        this.ISBN = ISBN;
        return this;
    }

    public BookDTOBuilder setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
        return this;
    }

    public BookDTOBuilder setFine(String fine) {
        this.fine = fine;
        return this;
    }

    public BookDTOBuilder setLanguage(String language) {
        this.language = language;
        return this;
    }

    public BookDTOBuilder setAuthors(List<AuthorDTO> authors) {
        this.authors = authors;
        return this;
    }

    public BookDTO createBookDTO() {
        return new BookDTO(id, name, count, ISBN, publicationDate, fine, language, authors);
    }
}
