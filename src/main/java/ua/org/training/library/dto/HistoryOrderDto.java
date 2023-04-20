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
public class HistoryOrderDto {
    private Long id;
    private String bookTitle;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateCreated;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateReturned;
    private StatusDto status;
}
