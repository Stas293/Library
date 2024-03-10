package ua.org.training.library.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class Order {
    private Long id;
    private LocalDate dateCreated;
    private LocalDate dateExpire;
    private Book book;
    private Status status;
    private User user;
    private Place place;
}
