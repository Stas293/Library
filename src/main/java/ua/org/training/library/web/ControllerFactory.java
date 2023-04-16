package ua.org.training.library.web;


import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.constants.Values;
import ua.org.training.library.context.HttpMapping;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.ControllerFactoryAnnotation;

import java.io.Serializable;
import java.util.Map;
import java.util.regex.Pattern;

@ControllerFactoryAnnotation
@Component
@Slf4j
public class ControllerFactory implements Serializable {
    @Setter
    private Map<String, HttpMapping> httpMapping;
    private final Pattern patternId = Pattern.compile("\\d+");
    private final Pattern patternParams = Pattern.compile("\\?.*");

    public HttpMapping getControllerCommand(String httpMethod, String httpPath) {
        httpPath = httpPath.replace(Values.LIBRARY_PATH, "");
        if (patternId.matcher(httpPath).find()) {
            httpPath = httpPath.replaceAll("\\d+", "{id}");
        }
        if (patternParams.matcher(httpPath).find()) {
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
