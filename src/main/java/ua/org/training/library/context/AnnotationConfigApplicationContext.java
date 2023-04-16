package ua.org.training.library.context;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.config.Config;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

        for (Map.Entry<Class<?>, Object> entry : cache.entrySet()) {
            if (checkIfClassIsPresent(tClass, entry, qualifiers))
                return tClass.cast(entry.getValue());
        }

        log.info("Creating new object {}", tClass);
        Class<? extends T> implClass = config.getImplClass(tClass, qualifiers);
        T t = objectFactory.create(implClass);

        log.info("Putting object {} to cache", t);
        cache.put(t.getClass(), t);

        return t;
    }

    private static <T> boolean checkIfClassIsPresent(Class<? extends T> tClass, Map.Entry<Class<?>, Object> entry, String[] qualifiers) {
        if (tClass.isAssignableFrom(entry.getKey())) {
            if (qualifiers.length == 1 &&
                    (entry.getKey().isAnnotationPresent(Component.class) &&
                            (entry.getKey().getAnnotation(Component.class).value()
                                    .equals(qualifiers[0])))) {
                log.info("Qualifier {} found for {}", qualifiers[0], tClass);
                log.info("Assigning object {} to {}", entry.getValue(), tClass);
                return true;
            } else if (qualifiers.length == 0) {
                log.info("No qualifier found for {}", tClass);
                log.info("Assigning object {} to {}", entry.getValue(), tClass);
                return true;
            }
        }
        return false;
    }

    public void close() {
        log.info("Closing context");
        synchronized (startupShutdownMonitor) {
            for (Object o : cache.values()) {
                log.info("Destroying object {}", o);
                objectFactory.destroy(o);
                cache.remove(o.getClass());
            }
        }
    }

    public Map<Class<?>, Object> getBeansWithAnnotation(Class<? extends Annotation> annotation) {
        Map<Class<?>, Object> beans = new ConcurrentHashMap<>();
        for (Map.Entry<Class<?>, Object> entry : cache.entrySet()) {
            if (entry.getKey().isAnnotationPresent(annotation)) {
                beans.put(entry.getKey(), entry.getValue());
            }
        }
        return beans;

    }

    public <T> Map<Class<?>, T> getBeansImplementingInterface(Class<T> interfaceClass) {
        Map<Class<?>, T> beans = new ConcurrentHashMap<>();
        for (Map.Entry<Class<?>, Object> entry : cache.entrySet()) {
            if (interfaceClass.isAssignableFrom(entry.getKey())) {
                beans.put(entry.getKey(), interfaceClass.cast(entry.getValue()));
            }
        }
        return beans;
    }
}