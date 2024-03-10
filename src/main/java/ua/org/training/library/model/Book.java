package ua.org.training.library.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Book {
    private Long id;
    private String title;
    private String description;
    private String isbn;
    private long count;
    private LocalDate publicationDate;
    private double fine;
    private String language;
    private String location;
    private List<Author> authors;
    private List<Keyword> keywords;
}
