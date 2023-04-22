package ua.org.training.library.mapping;


import lombok.SneakyThrows;
import ua.org.training.library.context.HttpMapping;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.mapping.Get;
import ua.org.training.library.enums.RequestMethod;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

@Component
public class GetMappingCommand implements HttpMappingCommand {
    @Override
    @SneakyThrows
    public HttpMapping createHttpMapping(Method method, String basePath, Object controller) {
        Get getMapping = method.getAnnotation(Get.class);
        if (getMapping != null) {
            String path = basePath + getMapping.value();
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            MethodHandle methodHandle = lookup.unreflect(method);
            return new HttpMapping(RequestMethod.GET.name(), path, methodHandle, controller);
        }
        return null;
    }
}
