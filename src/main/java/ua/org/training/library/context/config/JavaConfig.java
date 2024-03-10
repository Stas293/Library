package ua.org.training.library.context.config;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import ua.org.training.library.context.annotations.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Slf4j
public class JavaConfig implements Config {
    private final Set<Class<?>> reflectionsSet;

    public JavaConfig(String packageToScan) {
        reflectionsSet = new Reflections(packageToScan)
                .getTypesAnnotatedWith(Component.class);
    }

    public <T> Class<? extends T> getImplClass(Class<? extends T> interfaceClass) {
        Set<Class<? extends T>> classes = getClassesWhichImplementInterface(interfaceClass);
        log.info("interfaceClass: {}, classes: {}", interfaceClass, classes);
        if (classes.size() != 1) {
            throw new RuntimeException(interfaceClass + " has 0 or more than one impl, update your config");
        }
        return classes.iterator().next();
    }

    @NotNull
    @SuppressWarnings("unchecked")
    private <T> Set<Class<? extends T>> getClassesWhichImplementInterface(Class<? extends T> interfaceClass) {
        return reflectionsSet.stream()
                .filter(interfaceClass::isAssignableFrom)
                .map(aClass1 -> (Class<? extends T>) aClass1)
                .collect(Collectors.toSet());
    }

    @Override
    public <T> Class<? extends T> getImplClass(Class<? extends T> interfaceClass, String... qualifiers) {
        log.info("qualifiers: {}", Arrays.toString(qualifiers));
        log.info("Interface: {}", interfaceClass);
        if (qualifiers.length == 0) {
            return getImplClass(interfaceClass);
        }
        Set<Class<? extends T>> classes = getClassesAccordingToQualifier(interfaceClass, qualifiers);
        log.info("interfaceClass: {}, classes: {}, qualifiers: {}", interfaceClass, classes, qualifiers);
        if (classes.size() != 1) {
            throw new RuntimeException(interfaceClass + " has 0 or more than one impl, update your config");
        }
        return classes.iterator().next();
    }

    @NotNull
    @SuppressWarnings("unchecked")
    private <T> Set<Class<? extends T>> getClassesAccordingToQualifier(Class<? extends T> interfaceClass, String[] qualifiers) {
        return reflectionsSet.parallelStream()
                .filter(interfaceClass::isAssignableFrom)
                .map(aClass1 -> (Class<? extends T>) aClass1)
                .filter(aClass1 -> aClass1.isAnnotationPresent(Component.class))
                .filter(aClass1 -> aClass1.getAnnotation(Component.class).value().equals(qualifiers[0]))
                .collect(Collectors.toSet());
    }
}
