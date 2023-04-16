package ua.org.training.library.mapping;

import com.project.university.system_library.context.HttpMapping;
import com.project.university.system_library.context.annotations.Component;
import com.project.university.system_library.context.annotations.Put;
import com.project.university.system_library.enums.RequestMethod;

import java.lang.reflect.Method;

@Component
public class PutHttpMappingCommand implements HttpMappingCommand {
    @Override
    public HttpMapping createHttpMapping(Method method, String basePath, Object controller) {
        Put getMapping = method.getAnnotation(Put.class);
        if (getMapping != null) {
            String path = basePath + getMapping.value();
            return new HttpMapping(RequestMethod.PUT.name(), path, method, controller);
        }
        return null;
    }
}
