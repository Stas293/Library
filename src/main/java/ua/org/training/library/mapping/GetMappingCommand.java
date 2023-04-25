package ua.org.training.library.mapping;


import ua.org.training.library.context.HttpMapping;
import ua.org.training.library.context.annotations.ClassManagerType;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.mapping.Get;
import ua.org.training.library.enums.RequestMethod;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;

@Component
@ClassManagerType(values = {Get.class})
public class GetMappingCommand implements HttpMappingCommand {
    @Override
    public HttpMapping createHttpMapping(MethodHandle method,
                                         String path,
                                         Object controller,
                                         Annotation annotation) {
        Get get = (Get) annotation;
        String value = get.value();
        return new HttpMapping(RequestMethod.GET.name(), path + value, method, controller);
    }
}
