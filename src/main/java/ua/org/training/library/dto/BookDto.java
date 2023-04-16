package ua.org.training.library.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private Long id;
    private String title;
    private String description;
    private String isbn;
    private long count;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate publicationDate;
    private double fine;
    private String language;
    private String location;
    private String authors;
    private String keywords;
}
