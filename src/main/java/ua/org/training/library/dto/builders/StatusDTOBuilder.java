package ua.org.training.library.dto.builders;

import ua.org.training.library.dto.StatusDTO;
import ua.org.training.library.utility.Pair;

import java.util.List;

public class StatusDTOBuilder {
    private String code;
    private String name;
    private String value;
    private List<Pair<String, String>> nextStatuses;

    public StatusDTOBuilder setCode(String code) {
        this.code = code;
        return this;
    }

    public StatusDTOBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public StatusDTOBuilder setValue(String value) {
        this.value = value;
        return this;
    }

    public StatusDTOBuilder setNextStatuses(List<Pair<String, String>> nextStatuses) {
        this.nextStatuses = nextStatuses;
        return this;
    }

    public StatusDTO createStatusDTO() {
        return new StatusDTO(code, name, value, nextStatuses);
    }
}
