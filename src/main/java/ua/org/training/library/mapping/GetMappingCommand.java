package ua.org.training.library.mapping;


import ua.org.training.library.context.HttpMapping;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.Get;
import ua.org.training.library.enums.RequestMethod;

import java.lang.reflect.Method;

@Component
public class GetMappingCommand implements HttpMappingCommand {
    @Override
    public HttpMapping createHttpMapping(Method method, String basePath, Object controller) {
        Get getMapping = method.getAnnotation(Get.class);
        if (getMapping != null) {
            String path = basePath + getMapping.value();
            return new HttpMapping(RequestMethod.GET.name(), path, method, controller);
        }
        return null;
    }
}
