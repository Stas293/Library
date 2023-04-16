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
public class OrderDto {
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateCreated;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateExpire;
    private BookDto book;
    private StatusDto status;
    private UserDto user;
    private PlaceDto place;
}
