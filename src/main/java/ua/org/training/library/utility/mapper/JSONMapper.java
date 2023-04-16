package ua.org.training.library.utility.mapper;

import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.SneakyThrows;
import ua.org.training.library.context.annotations.Component;

import java.io.Writer;

@Component
public class JSONMapper {
    private final JsonMapper mapper;

    public JSONMapper() {
        this.mapper = new JsonMapper();
        mapper.findAndRegisterModules();
    }

    @SneakyThrows
    public <T> void toJson(Writer writer, T object) {
        mapper.writeValue(writer, object);
    }
}
