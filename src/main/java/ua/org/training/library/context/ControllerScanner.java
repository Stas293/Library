package ua.org.training.library.context;


import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Controller;
import ua.org.training.library.context.annotations.ControllerFactoryAnnotation;
import ua.org.training.library.mapping.HttpMappingCommand;
import ua.org.training.library.web.ControllerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ControllerScanner {

    private final AnnotationConfigApplicationContext applicationContext;

    public ControllerScanner(AnnotationConfigApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void scanControllers() {
        Map<String, HttpMapping> mappings = new HashMap<>();
        Map<Class<?>, HttpMappingCommand> httpMappingCommands =
                applicationContext.getBeansImplementingInterface(HttpMappingCommand.class);
        Map<Class<?>, Object> beans = applicationContext.getBeansWithAnnotation(Controller.class);
        log.info("Found {} controllers", beans.size());
        for (Object controller : beans.values()) {
            Class<?> clazz = controller.getClass();
            String basePath = clazz.getAnnotation(Controller.class).value();
            if (basePath.isEmpty()) {
                basePath = "";
            }

            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                for (HttpMappingCommand command : httpMappingCommands.values()) {
                    HttpMapping mapping = command.createHttpMapping(method, basePath, controller);
                    if (mapping != null) {
                        mappings.put(mapping.httpMethod() + ":" + mapping.httpPath(), mapping);
                    }
                }
            }
        }

        setControllerFactory(mappings);
    }

    private void setControllerFactory(Map<String, HttpMapping> mappings) {
        Map<Class<?>, Object> controllerFactoryMap = applicationContext.getBeansWithAnnotation(ControllerFactoryAnnotation.class);
        if (controllerFactoryMap.size() == 1) {
            ControllerFactory factory = (ControllerFactory) controllerFactoryMap.get(ControllerFactory.class);
            factory.setHttpMapping(mappings);
        }
    }
}

