package ua.org.training.library.context.config;

import java.util.Set;

public interface Config {
    <T> Class<? extends T> getImplClass(Class<? extends T> interfaceClass, String... qualifiers);

    Set<Class<?>> getReflectionsSet();
}
