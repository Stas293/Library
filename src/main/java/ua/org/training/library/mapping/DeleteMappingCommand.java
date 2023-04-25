package ua.org.training.library.mapping;


import ua.org.training.library.context.HttpMapping;
import ua.org.training.library.context.annotations.ClassManagerType;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.mapping.Delete;
import ua.org.training.library.enums.RequestMethod;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;

@Component
@ClassManagerType(values = {Delete.class})
public class DeleteMappingCommand implements HttpMappingCommand {
    @Override
    public HttpMapping createHttpMapping(MethodHandle method,
                                         String path,
                                         Object controller,
                                         Annotation annotation) {
        Delete delete = (Delete) annotation;
        String value = delete.value();
        return new HttpMapping(RequestMethod.DELETE.name(), path + value, method, controller);
    }
}
