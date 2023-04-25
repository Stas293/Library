package ua.org.training.library.mapping;


import ua.org.training.library.context.HttpMapping;
import ua.org.training.library.context.annotations.ClassManagerType;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.mapping.Post;
import ua.org.training.library.enums.RequestMethod;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;

@Component
@ClassManagerType(values = {Post.class})
public class PostMappingCommand implements HttpMappingCommand {
    @Override
    public HttpMapping createHttpMapping(MethodHandle method,
                                         String path,
                                         Object controller,
                                         Annotation annotation) {
        Post post = (Post) annotation;
        String value = post.value();
        return new HttpMapping(RequestMethod.POST.name(), path + value, method, controller);
    }
}
