package ua.org.training.library.context.configurator;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.AnnotationConfigApplicationContext;
import ua.org.training.library.context.annotations.ClassManagerType;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.InjectProperty;
import ua.org.training.library.enums.DefaultValues;
import ua.org.training.library.field_setters.FieldSetter;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InjectPropertyAnnotationObjectConfigurator implements ObjectConfigurator {
    private static final String APP_PROPERTIES_NAME = "application";
    private final Map<String, String> propertiesMap;
    private final Map<Class<?>, FieldSetter> fieldSetters;

    @SneakyThrows
    public InjectPropertyAnnotationObjectConfigurator() {
        log.info("Initializing properties");
        ResourceBundle bundle = ResourceBundle.getBundle(
                APP_PROPERTIES_NAME,
                Locale.of(DefaultValues.APP_DEFAULT_LOCALE.getValue()));
        propertiesMap = bundle.keySet().stream()
                .map(key -> Map.entry(key, getEnvOrProperty(bundle.getString(key))))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        fieldSetters = new HashMap<>();
    }

    private String getEnvOrProperty(String s) {
        if (s.startsWith("${") && s.endsWith("}")) {
            String env = s.substring(2, s.length() - 1);
            return Optional.ofNullable(System.getenv(env)).orElse(s);
        }
        return s;
    }

    @Override
    public void configure(Object t, AnnotationConfigApplicationContext context) {
        log.info("Configuring properties");
        Map<Class<?>, FieldSetter> beansImplementingInterface =
                context.getBeansImplementingInterface(FieldSetter.class);
        beansImplementingInterface.forEach(this::fillMapWithFieldSetters);
        Arrays.stream(t.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(InjectProperty.class))
                .forEach(field -> injectProperty(t, field));
    }

    @SneakyThrows
    private void injectProperty(Object t,Field field) {
        log.info("Injecting property");
        log.info("Field name: {}", field.getName());
        log.info("Field type: {}", field.getType());
        InjectProperty annotation = field.getAnnotation(InjectProperty.class);
        String propertyName = annotation.value();
        if (propertyName.isEmpty()) {
            propertyName = field.getName();
        }
        String propertyValue = propertiesMap.get(propertyName);
        field.setAccessible(true);
        Type type = field.getType();
        setFieldDependingOnType(t, field, propertyValue, type);
        field.setAccessible(false);
    }

    private void fillMapWithFieldSetters(Class<?> key, FieldSetter value) {
        ClassManagerType annotation = key.getAnnotation(ClassManagerType.class);
        if (annotation == null) {
            throw new RuntimeException("ClassManagerType annotation is not present");
        }
        Class<?>[] classesToCastTo = annotation.values();
        Arrays.stream(classesToCastTo)
                .forEach(classToCastTo -> fieldSetters.put(classToCastTo, value));
    }

    private void setFieldDependingOnType(Object t, Field field, String propertyValue, Type type)
            throws IllegalAccessException {
        FieldSetter fieldSetter = fieldSetters.get(type);
        if (fieldSetter == null) {
            throw new RuntimeException("Unsupported type: " + type);
        }
        fieldSetter.setField(field, t, propertyValue);
    }
}
