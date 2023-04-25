package ua.org.training.library.mapping;


import ua.org.training.library.context.HttpMapping;
import ua.org.training.library.context.annotations.ClassManagerType;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.mapping.Patch;
import ua.org.training.library.enums.RequestMethod;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;

@Component
@ClassManagerType(values = {Patch.class})
public class PatchMappingCommand implements HttpMappingCommand {
    @Override
    public HttpMapping createHttpMapping(MethodHandle method, String path, Object controller, Annotation annotation) {
        Patch patch = (Patch) annotation;
        String value = patch.value();
        return new HttpMapping(RequestMethod.PATCH.name(), path + value, method, controller);
    }
}
