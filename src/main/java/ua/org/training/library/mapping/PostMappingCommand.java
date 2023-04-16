package ua.org.training.library.mapping;

import com.project.university.system_library.context.HttpMapping;
import com.project.university.system_library.context.annotations.Component;
import com.project.university.system_library.context.annotations.Post;
import com.project.university.system_library.enums.RequestMethod;

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
