package ua.org.training.library.mapping;


import ua.org.training.library.context.HttpMapping;
import ua.org.training.library.context.annotations.ClassManagerType;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.mapping.Put;
import ua.org.training.library.enums.RequestMethod;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;

@Component
@ClassManagerType(values = {Put.class})
public class PutHttpMappingCommand implements HttpMappingCommand {
    @Override
    public HttpMapping createHttpMapping(MethodHandle method, String path, Object controller, Annotation annotation) {
        Put put = (Put) annotation;
        String value = put.value();
        return new HttpMapping(RequestMethod.PUT.name(), path + value, method, controller);
    }
}
