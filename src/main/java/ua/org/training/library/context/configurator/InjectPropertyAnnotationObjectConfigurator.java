package ua.org.training.library.context.configurator;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.constants.Values;
import ua.org.training.library.context.AnnotationConfigApplicationContext;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.InjectProperty;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InjectPropertyAnnotationObjectConfigurator implements ObjectConfigurator {
    private static final String APP_PROPERTIES_NAME = "application";
    private final Map<String, String> propertiesMap;

    @SneakyThrows
    public InjectPropertyAnnotationObjectConfigurator() {
        log.info("Initializing properties");
        ResourceBundle bundle = ResourceBundle.getBundle(
                APP_PROPERTIES_NAME,
                Locale.of(Values.APP_DEFAULT_LOCALE));
        propertiesMap = bundle.keySet().stream()
                .map(key -> Map.entry(key, getEnvOrProperty(bundle.getString(key))))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private String getEnvOrProperty(String s) {
        if (s.startsWith("${") && s.endsWith("}")) {
            String env = s.substring(2, s.length() - 1);
            return Optional.ofNullable(System.getenv(env)).orElse(s);
        }
        return s;
    }

    @Override
    @SneakyThrows
    public void configure(Object t, AnnotationConfigApplicationContext context) {
        log.info("Configuring properties");
        Class<?> implClass = t.getClass();
        for (Field field : implClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(InjectProperty.class)) {
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
                Type type = field.getGenericType();
                setFieldDependingOnType(t, field, propertyValue, type);
                field.setAccessible(false);
            }
        }
    }

    private static void setFieldDependingOnType(Object t, Field field, String propertyValue, Type type)
            throws IllegalAccessException {
        if (type.equals(int.class)) {
            field.set(t, Integer.parseInt(propertyValue));
        } else if (type.equals(long.class)) {
            field.set(t, Long.parseLong(propertyValue));
        } else if (type.equals(double.class)) {
            field.set(t, Double.parseDouble(propertyValue));
        } else if (type.equals(boolean.class)) {
            field.set(t, Boolean.parseBoolean(propertyValue));
        } else if (type.equals(String.class)) {
            field.set(t, propertyValue);
        } else throw new RuntimeException("Unsupported type: " + type);
    }
}
