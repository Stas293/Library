package ua.org.training.library.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class HistoryOrder {
    private Long id;
    private String bookTitle;
    private LocalDate dateCreated;
    private LocalDate dateReturned;
    private Status status;
    private User user;
}
