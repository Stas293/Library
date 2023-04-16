package ua.org.training.library.context.proxy;


import ua.org.training.library.context.AnnotationConfigApplicationContext;

public interface ProxyConfigurator {
    <T> T getProxyObjectWhenNeeded(T object, Class<?> implClass, AnnotationConfigApplicationContext context);
}
