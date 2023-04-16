package ua.org.training.library.context.configurator;


import ua.org.training.library.context.AnnotationConfigApplicationContext;

public interface ObjectConfigurator {
    void configure(Object object, AnnotationConfigApplicationContext context);
}
