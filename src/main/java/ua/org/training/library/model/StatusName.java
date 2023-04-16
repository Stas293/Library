package ua.org.training.library.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusName {
    private Long id;
    private String name;
    private String lang;
    private Status status;
}
