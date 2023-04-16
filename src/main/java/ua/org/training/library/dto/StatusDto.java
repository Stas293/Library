package ua.org.training.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusDto {
    private String code;
    private String value;
    private Map<String, StatusDto> nextStatuses;
    private Boolean closed;
}
