package ua.org.training.library.context;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.config.Config;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
public class AnnotationConfigApplicationContext {
    private final Map<Class<?>, Object> cache = new ConcurrentHashMap<>();
    private final Object startupShutdownMonitor = new Object();
    @Getter
    private final Config config;
    @Setter
    private ObjectFactory objectFactory;

    public AnnotationConfigApplicationContext(Config config) {
        this.config = config;
    }

    public <T> T getObject(Class<? extends T> tClass, String... qualifiers) {
        log.info("Getting object {}, qualifiers {}", tClass, qualifiers);
        if (cache.containsKey(tClass)) {
            log.info("Cache contains object {}", tClass);
            return tClass.cast(cache.get(tClass));
        }

        T object = cache.entrySet().parallelStream()
                .filter(entry -> entry.getKey().isAssignableFrom(tClass))
                .filter(entry -> checkIfClassIsPresent(tClass, entry, qualifiers))
                .map(entry -> tClass.cast(entry.getValue()))
                .findFirst()
                .orElse(null);
        if (object != null) {
            return object;
        }

        log.info("Creating new object {}", tClass);
        Class<? extends T> implClass = config.getImplClass(tClass, qualifiers);
        T t = objectFactory.create(implClass);

        log.info("Putting object {} to cache", t);
        cache.put(t.getClass(), t);

        return t;
    }

    private <T> boolean checkIfClassIsPresent(Class<? extends T> tClass,
                                              Map.Entry<Class<?>, Object> entry,
                                              String[] qualifiers) {
        if (qualifiers.length == 1 &&
                (entry.getKey().isAnnotationPresent(Component.class) &&
                        (entry.getKey().getAnnotation(Component.class)
                                .value().equals(qualifiers[0])))) {
            log.info("Qualifier {} found for {}", qualifiers[0], tClass);
            log.info("Assigning object {} to {}", entry.getValue(), tClass);
            return true;
        } else if (qualifiers.length == 0) {
            log.info("No qualifier found for {}", tClass);
            log.info("Assigning object {} to {}", entry.getValue(), tClass);
            return true;
        }
        return false;
    }

    public void close() {
        log.info("Closing context");
        synchronized (startupShutdownMonitor) {
            cache.values().forEach(o -> {
                log.info("Destroying object {}", o);
                objectFactory.destroy(o);
                cache.remove(o.getClass());
            });
        }
    }

    public Map<Class<?>, Object> getBeansWithAnnotation(Class<? extends Annotation> annotation) {
        return cache.entrySet().parallelStream()
                .filter(entry -> entry.getKey().isAnnotationPresent(annotation))
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> b));

    }

    public <T> Map<Class<?>, T> getBeansImplementingInterface(Class<T> interfaceClass) {
        return cache.entrySet().parallelStream()
                .filter(entry -> interfaceClass.isAssignableFrom(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> interfaceClass.cast(entry.getValue()),
                        (a, b) -> b));
    }
}
