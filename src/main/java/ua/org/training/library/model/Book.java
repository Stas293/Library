package ua.org.training.library.model;

import ua.org.training.library.model.builders.BookBuilder;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;

public class Book implements Serializable {
    private long id;
    private String name;
    private int count;
    private String ISBN;
    private Date publicationDate;
    private double fine;
    private String language;
    private Collection<Author> authors = new HashSet<>();

    public Book() {
    }

    public Book(long id, String name, int count, String ISBN, Date publicationDate, double fine, String language, Collection<Author> authors) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.ISBN = ISBN;
        this.publicationDate = publicationDate;
        this.fine = fine;
        this.language = language;
        this.authors = authors;
    }

    public static BookBuilder builder() {
        return new BookBuilder();
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

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public double getFine() {
        return fine;
    }

    public void setFine(double fine) {
        this.fine = fine;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Collection<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Collection<Author> authors) {
        this.authors = authors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book book)) return false;

        if (id != book.id) return false;
        if (count != book.count) return false;
        if (Double.compare(book.fine, fine) != 0) return false;
        if (!name.equals(book.name)) return false;
        if (!ISBN.equals(book.ISBN)) return false;
        if (!publicationDate.equals(book.publicationDate)) return false;
        if (!language.equals(book.language)) return false;
        return Objects.equals(authors, book.authors);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + name.hashCode();
        result = 31 * result + count;
        result = 31 * result + ISBN.hashCode();
        result = 31 * result + publicationDate.hashCode();
        temp = Double.doubleToLongBits(fine);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + language.hashCode();
        result = 31 * result + (authors != null ? authors.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", count=" + count +
                ", ISBN='" + ISBN + '\'' +
                ", publicationDate=" + publicationDate +
                ", fine=" + fine +
                ", language='" + language + '\'' +
                ", authors=" + authors +
                '}';
    }
}
