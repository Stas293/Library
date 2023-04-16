package ua.org.training.library.context;


import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.Qualifier;
import ua.org.training.library.context.configurator.ObjectConfigurator;
import ua.org.training.library.context.proxy.ProxyConfigurator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class ObjectFactory {
    private final List<? extends ObjectConfigurator> configurators;
    private final List<? extends ProxyConfigurator> proxyConfigurators;
    private final AnnotationConfigApplicationContext context;

    public ObjectFactory(AnnotationConfigApplicationContext context) {
        this.context = context;
        configurators = context.getConfig()
                .getReflectionsSet()
                .parallelStream()
                .filter(ObjectConfigurator.class::isAssignableFrom)
                .map(ObjectFactory::getClassInstance)
                .map(t -> (ObjectConfigurator) t)
                .toList();
        proxyConfigurators = context.getConfig()
                .getReflectionsSet()
                .parallelStream()
                .filter(ProxyConfigurator.class::isAssignableFrom)
                .map(ObjectFactory::getClassInstance)
                .map(t -> (ProxyConfigurator) t)
                .toList();
    }

    @NotNull
    @SneakyThrows
    private static <T> T getClassInstance(Class<T> t) {
        return t.getConstructor().newInstance();
    }

    private <T> void runPostConstruct(Class<? extends T> implClass, T t) {
        Arrays.stream(implClass.getMethods())
                .filter(method -> method.isAnnotationPresent(PostConstruct.class))
                .forEach(method -> {
                    try {
                        method.invoke(t);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @SneakyThrows
    public <T> T create(Class<? extends T> implClass) {
        log.info("Creating new object {}", implClass);
        T t = createInstanceClass(implClass);

        log.info("Configuring object {}", t);
        configure(t);

        log.info("Running post construct for object {}", t);
        runPostConstruct(implClass, t);

        log.info("Wrapping object {}", t);
        t = wrapWithProxy(implClass, t);

        log.info("Object created {}", t);
        return t;
    }

    private <T> T wrapWithProxy(Class<? extends T> implClass, T t) {
        for (ProxyConfigurator proxyConfigurator : proxyConfigurators) {
            t = proxyConfigurator.getProxyObjectWhenNeeded(t, implClass, context);
        }
        return t;
    }

    private <T> void configure(T t) {
        configurators.forEach(configurator -> configurator.configure(t, context));
    }

    @NotNull
    private <T> T createInstanceClass(Class<? extends T> implClass)
            throws InstantiationException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        Set<Constructor<?>> constructors = getAutowiredConstructors(implClass);
        log.info("Constructors: {}", constructors);
        if (constructors.size() > 1) {
            throw new RuntimeException("More than one constructor with @Autowired");
        } else if (constructors.isEmpty()) {
            return implClass.getDeclaredConstructor().newInstance();
        }
        Constructor<?> constructor = constructors.stream().findFirst().orElseThrow();
        log.info("Constructor: {}", constructor);
        Set<Object> qualifiedObjects = getObjectsWithQualifierField(constructor);
        Set<Object> objects = getObjectsWithoutQualifierField(constructor);
        Object[] args = Arrays.stream(constructor.getParameters())
                .map(parameter -> getObjectForConstructorInvocation(qualifiedObjects, objects, parameter))
                .toArray();
        log.info("Args: {}", Arrays.toString(args));
        return (T) constructor.newInstance(args);
    }

    @Nullable
    private Object getObjectForConstructorInvocation(Set<Object> qualifiedObjects,
                                                            Set<Object> objects, Parameter parameter) {
        log.info("Parameter: {}", parameter);
        log.info("Parameter type: {}", parameter.getType());
        if (parameter.isAnnotationPresent(Qualifier.class)) {
            log.info("Qualifier: {}", parameter.getAnnotation(Qualifier.class).value());
            return qualifiedObjects.stream()
                    .filter(o -> o.getClass().getAnnotation(Component.class).value()
                            .equals(parameter.getAnnotation(Qualifier.class).value()))
                    .findFirst()
                    .orElse(null);
        } else {
            log.info("No qualifier");
            return objects.stream()
                    .filter(o -> parameter.getType().isAssignableFrom(o.getClass()))
                    .findFirst()
                    .orElse(null);
        }
    }

    @NotNull
    private Set<Object> getObjectsWithoutQualifierField(Constructor<?> constructor) {
        Set<Object> objects = Arrays.stream(constructor.getParameters())
                .filter(parameter -> !parameter.isAnnotationPresent(Qualifier.class))
                .map(Parameter::getType)
                .map(t -> context.getObject(t))
                .collect(Collectors.toSet());
        log.info("Objects: {}", objects);
        return objects;
    }

    @NotNull
    private Set<Object> getObjectsWithQualifierField(Constructor<?> constructor) {
        Set<Object> qualifiedObjects = Arrays.stream(constructor.getParameters())
                .filter(parameter -> parameter.isAnnotationPresent(Qualifier.class))
                .map(t -> context.getObject(t.getType(), t.getAnnotation(Qualifier.class).value()))
                .collect(Collectors.toSet());
        log.info("Qualified objects: {}", qualifiedObjects);
        return qualifiedObjects;
    }

    @NotNull
    private <T> Set<Constructor<?>> getAutowiredConstructors(Class<T> implClass) {
        return Arrays.stream(implClass.getConstructors())
                .filter(constructor -> constructor.isAnnotationPresent(Autowired.class))
                .collect(Collectors.toSet());
    }


    public void destroy(Object o) {
        runPreDestroy(o);

        closeIfAutoCloseable(o);
    }

    private void closeIfAutoCloseable(Object o) {
        if (o instanceof AutoCloseable autoCloseable) {
            log.info("Closing object AutoCloseable {}", o);
            try {
                autoCloseable.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void runPreDestroy(Object o) {
        Method[] methods = o.getClass().getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(PreDestroy.class)) {
                log.info("Running pre destroy for object {} method {}", o, method);
                try {
                    method.invoke(o);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
