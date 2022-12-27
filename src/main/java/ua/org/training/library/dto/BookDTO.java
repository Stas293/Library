package ua.org.training.library.dto;

import ua.org.training.library.model.Book;
import ua.org.training.library.utility.Utility;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;

public class BookDTO implements Serializable {
    private long id;
    private String name;
    private int count;
    private String ISBN;
    private String publicationDate;
    private String fine;
    private String language;
    private List<AuthorDTO> authors;

    public BookDTO(Locale locale, Book book) {
        this.id = book.getId();
        this.name = book.getName();
        this.count = book.getCount();
        this.ISBN = book.getISBN();
        this.publicationDate = book.getPublicationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString();
        this.fine = Utility.getLocaleFine(locale, book.getFine());
        this.language = book.getLanguage();
        this.authors = book.getAuthors().stream()
                .map(AuthorDTO::new)
                .toList();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getFine() {
        return fine;
    }

    public void setFine(String fine) {
        this.fine = fine;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<AuthorDTO> getAuthors() {
        return authors;
    }

    public void setAuthors(List<AuthorDTO> authors) {
        this.authors = authors;
    }

    @Override
    public String toString() {
        return "BookDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", count=" + count +
                ", ISBN='" + ISBN + '\'' +
                ", publicationDate='" + publicationDate + '\'' +
                ", fine='" + fine + '\'' +
                ", language='" + language + '\'' +
                ", authors=" + authors +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookDTO bookDTO)) return false;

        if (id != bookDTO.id) return false;
        if (count != bookDTO.count) return false;
        if (!name.equals(bookDTO.name)) return false;
        if (!ISBN.equals(bookDTO.ISBN)) return false;
        if (!publicationDate.equals(bookDTO.publicationDate)) return false;
        if (!fine.equals(bookDTO.fine)) return false;
        if (!language.equals(bookDTO.language)) return false;
        return authors.equals(bookDTO.authors);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + name.hashCode();
        result = 31 * result + count;
        result = 31 * result + ISBN.hashCode();
        result = 31 * result + publicationDate.hashCode();
        result = 31 * result + fine.hashCode();
        result = 31 * result + language.hashCode();
        result = 31 * result + authors.hashCode();
        return result;
    }
}
