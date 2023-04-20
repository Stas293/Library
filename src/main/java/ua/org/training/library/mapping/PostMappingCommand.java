package ua.org.training.library.mapping;


import ua.org.training.library.context.HttpMapping;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.mapping.Post;
import ua.org.training.library.enums.RequestMethod;

import java.lang.reflect.Method;

@Component
public class PostMappingCommand implements HttpMappingCommand {
    @Override
    public HttpMapping createHttpMapping(Method method, String basePath, Object controller) {
        Post postMapping = method.getAnnotation(Post.class);
        if (postMapping != null) {
            String path = basePath + postMapping.value();
            return new HttpMapping(RequestMethod.POST.name(), path, method, controller);
        }
        return null;
    }
}
