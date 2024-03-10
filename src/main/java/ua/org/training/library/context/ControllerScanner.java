package ua.org.training.library.context;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import ua.org.training.library.context.annotations.ClassManagerType;
import ua.org.training.library.context.annotations.Controller;
import ua.org.training.library.context.annotations.ControllerFactoryAnnotation;
import ua.org.training.library.mapping.HttpMappingCommand;
import ua.org.training.library.web.ControllerFactory;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class ControllerScanner {

    private final AnnotationConfigApplicationContext applicationContext;

    @SneakyThrows
    public void scanControllers() {
        Map<String, HttpMapping> mappings = new ConcurrentHashMap<>();
        Map<Class<?>, Object> methodMap = getMethodMappings();
        Map<Class<?>, Object> beans = applicationContext.getBeansWithAnnotation(Controller.class);
        log.info("Found {} controllers", beans.size());
        for (Object controller : beans.values()) {
            Class<?> clazz = controller.getClass();
            String basePath = clazz.getAnnotation(Controller.class).value();
            if (basePath.isEmpty()) {
                basePath = "";
            }

            Method[] methods = clazz.getMethods();
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            for (Method method : methods) {
                if (method.getAnnotations().length == 0) {
                    continue;
                }
                Annotation annotation = method.getAnnotations()[0];
                if (methodMap.containsKey(annotation.annotationType())) {
                    HttpMapping mapping = ((HttpMappingCommand) methodMap.get(annotation.annotationType()))
                            .createHttpMapping(lookup.unreflect(method), basePath, controller, annotation);
                    mappings.put(mapping.httpMethod() + ":" + mapping.httpPath(), mapping);
                }
            }
        }

        setControllerFactory(mappings);
    }

    @NotNull
    private Map<Class<?>, Object> getMethodMappings() {
        Map<Class<?>, HttpMappingCommand> httpMappingCommands =
                applicationContext.getBeansImplementingInterface(HttpMappingCommand.class);
        return httpMappingCommands.entrySet().stream()
                .collect(Collectors.toMap(key -> key.getKey().getAnnotation(ClassManagerType.class).values()[0],
                        Map.Entry::getValue));
    }

    private void setControllerFactory(Map<String, HttpMapping> mappings) {
        Map<Class<?>, Object> controllerFactoryMap = applicationContext.getBeansWithAnnotation(ControllerFactoryAnnotation.class);
        if (controllerFactoryMap.size() == 1) {
            ControllerFactory factory = (ControllerFactory) controllerFactoryMap.get(ControllerFactory.class);
            factory.setHttpMapping(mappings);
            return;
        }
        throw new RuntimeException("ControllerFactory not found or more than one");
    }
}

