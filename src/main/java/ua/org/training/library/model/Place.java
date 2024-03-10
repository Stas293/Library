package ua.org.training.library.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Place {
    private Long id;
    private String code;
    private List<PlaceName> names;
    private Integer defaultDays;
    private Boolean choosable;
}
