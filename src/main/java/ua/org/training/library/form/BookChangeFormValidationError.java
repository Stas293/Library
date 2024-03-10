package ua.org.training.library.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookChangeFormValidationError {
    private String title;
    private String description;
    private String isbn;
    private String count;
    private String publicationDate;
    private String fine;
    private String language;
    private String location;
    private String authors;
    private String keywords;
    private boolean containsErrors = false;

    public void setTitle(String title) {
        this.title = title;
        this.containsErrors = true;
    }

    public void setDescription(String description) {
        this.description = description;
        this.containsErrors = true;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
        this.containsErrors = true;
    }

    public void setCount(String count) {
        this.count = count;
        this.containsErrors = true;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
        this.containsErrors = true;
    }

    public void setFine(String fine) {
        this.fine = fine;
        this.containsErrors = true;
    }

    public void setLanguage(String language) {
        this.language = language;
        this.containsErrors = true;
    }

    public void setLocation(String location) {
        this.location = location;
        this.containsErrors = true;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
        this.containsErrors = true;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
        this.containsErrors = true;
    }
}
