package ua.org.training.library.mapping;


import ua.org.training.library.context.HttpMapping;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.mapping.Patch;
import ua.org.training.library.enums.RequestMethod;

import java.lang.reflect.Method;

@Component
public class PatchMappingCommand implements HttpMappingCommand {
    @Override
    public HttpMapping createHttpMapping(Method method, String basePath, Object controller) {
        Patch postMapping = method.getAnnotation(Patch.class);
        if (postMapping != null) {
            String path = basePath + postMapping.value();
            return new HttpMapping(RequestMethod.PATCH.name(), path, method, controller);
        }
        return null;
    }
}
