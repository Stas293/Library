package ua.org.training.library.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Status {
    private Long id;
    private List<StatusName> names;
    private String code;
    private Boolean closed;
    private List<Status> nextStatuses;
}
