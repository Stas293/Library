package ua.org.training.library.mapping;


import ua.org.training.library.context.HttpMapping;

import java.lang.reflect.Method;

public interface HttpMappingCommand {
    HttpMapping createHttpMapping(Method method, String basePath, Object controller);
}

