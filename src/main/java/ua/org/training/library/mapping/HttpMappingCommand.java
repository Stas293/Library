package ua.org.training.library.mapping;


import ua.org.training.library.context.HttpMapping;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;

public interface HttpMappingCommand {
    HttpMapping createHttpMapping(MethodHandle method,
                                  String basePath,
                                  Object controller,
                                  Annotation annotation);
}

