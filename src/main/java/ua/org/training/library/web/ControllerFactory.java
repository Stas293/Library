package ua.org.training.library.web;


import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.HttpMapping;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.ControllerFactoryAnnotation;

import java.io.Serializable;
import java.util.Map;
import java.util.regex.Pattern;

import static ua.org.training.library.enums.DefaultValues.LIBRARY_PATH;

@ControllerFactoryAnnotation
@Component
@Slf4j
public class ControllerFactory implements Serializable {
    private static final Pattern PATTERN_ID = Pattern.compile("\\d+");
    private static final Pattern PATTERN_PARAMS = Pattern.compile("\\?.*");
    @Setter
    private Map<String, HttpMapping> httpMapping;

    public HttpMapping getControllerCommand(String httpMethod, String httpPath) {
        httpPath = httpPath.replace(LIBRARY_PATH.getValue(), "");
        if (PATTERN_ID.matcher(httpPath).find()) {
            httpPath = httpPath.replaceAll("\\d+", "{id}");
        }
        if (PATTERN_PARAMS.matcher(httpPath).find()) {
            httpPath = httpPath.replaceAll("\\?.*", "");
        }
        HttpMapping mapping = httpMapping.get(httpMethod.toUpperCase() + ":" + httpPath);
        if (mapping == null) {
            log.error("No mapping found for {} {}", httpMethod, httpPath);
            return httpMapping.get("GET:/error");
        }
        return mapping;
    }
}
