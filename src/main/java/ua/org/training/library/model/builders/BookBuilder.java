package ua.org.training.library.model.builders;

import ua.org.training.library.model.Author;
import ua.org.training.library.model.Book;

import java.util.Collection;
import java.util.Date;

public class BookBuilder {
    private String name;
    private long id;
    private int count;
    private String isbn;
    private Date publicationDate;
    private double fine;
    private String language;
    private Collection<Author> authors;

    public BookBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public BookBuilder setId(long id) {
        this.id = id;
        return this;
    }

    public BookBuilder setCount(int count) {
        this.count = count;
        return this;
    }

    public BookBuilder setISBN(String isbn) {
        this.isbn = isbn;
        return this;
    }

    public BookBuilder setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
        return this;
    }

    public BookBuilder setFine(double fine) {
        this.fine = fine;
        return this;
    }

    public BookBuilder setLanguage(String language) {
        this.language = language;
        return this;
    }

    public BookBuilder setAuthors(Collection<Author> authors) {
        this.authors = authors;
        return this;
    }

    public Book createBook() {
        return new Book(id, name, count, isbn, publicationDate, fine, language, authors);
    }
}