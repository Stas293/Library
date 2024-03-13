package ua.org.training.library.context.configurator;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.AnnotationConfigApplicationContext;
import ua.org.training.library.context.annotations.ClassManagerType;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.InjectProperty;
import ua.org.training.library.enums.DefaultValues;
import ua.org.training.library.field_setters.FieldSetter;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.validator.constraint_validators.ConstraintValidator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

import static ua.org.training.library.utility.Utility.getTypeParametersFromString;

@Component
@Slf4j
public class InjectPropertyAnnotationObjectConfigurator implements ObjectConfigurator {
    private static final String APP_PROPERTIES_NAME = "application";
    private final Map<String, String> propertiesMap;
    private final Map<Class<?>, FieldSetter> fieldSetters;
    private final Map<Class<? extends Annotation>, ConstraintValidator> constraintValidators;
    private int lastFieldSetterCount;
    private int lastConstraintValidatorCount;

    @SneakyThrows
    public InjectPropertyAnnotationObjectConfigurator() {
        log.info("Initializing properties");
        ResourceBundle bundle = ResourceBundle.getBundle(
                APP_PROPERTIES_NAME,
                Locale.of(DefaultValues.APP_DEFAULT_LOCALE.getValue()));
        propertiesMap = bundle.keySet().stream()
                .map(key -> Map.entry(key, Utility.getEnvOrProperty(bundle.getString(key))))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        fieldSetters = new HashMap<>();
        constraintValidators = new HashMap<>();
        lastFieldSetterCount = 0;
    }


    @Override
    public void configure(Object t, AnnotationConfigApplicationContext context) {
        log.info("Configuring properties");
        Map<Class<?>, FieldSetter> beansImplementingFieldSetter =
                context.getBeansImplementingInterface(FieldSetter.class);
        Map<Class<?>, ConstraintValidator> beansImplementingConstraintValidator =
                context.getBeansImplementingInterface(ConstraintValidator.class);
        if (lastFieldSetterCount != beansImplementingFieldSetter.size()) {
            lastFieldSetterCount = beansImplementingFieldSetter.size();
            beansImplementingFieldSetter.forEach(this::fillMapWithFieldSetters);
        }
        if (lastConstraintValidatorCount != beansImplementingConstraintValidator.size()) {
            lastConstraintValidatorCount = beansImplementingConstraintValidator.size();
            beansImplementingConstraintValidator.forEach(this::fillMapWithConstraintValidators);
        }
        Arrays.stream(t.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(InjectProperty.class))
                .forEach(field -> injectProperty(t, field));
    }

    @SneakyThrows
    private void injectProperty(Object t, Field field) {
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
        setValidateFieldDependingOnType(t, field, propertyValue, type);
        field.setAccessible(false);
    }

    private void fillMapWithFieldSetters(Class<?> key, FieldSetter value) {
        ClassManagerType annotation = key.getAnnotation(ClassManagerType.class);
        if (annotation == null) {
            throw new RuntimeException("ClassManagerType annotation is not present");
        }
        Class<?>[] classesToCastTo = annotation.values();
        Arrays.stream(classesToCastTo)
                .filter(classToCastTo -> !fieldSetters.containsKey(classToCastTo))
                .forEach(classToCastTo -> fieldSetters.put(classToCastTo, value));
    }

    private void fillMapWithConstraintValidators(Class<?> key, ConstraintValidator value) {
        String typeName = value.getClass().getGenericInterfaces()[0].getTypeName();
        Type[] typeParameters = getTypeParametersFromString(typeName);
        constraintValidators.put((Class<? extends Annotation>) typeParameters[0], value);
    }

    private void setValidateFieldDependingOnType(Object t, Field field, String propertyValue, Type type)
            throws IllegalAccessException {
        FieldSetter fieldSetter = fieldSetters.get(type);
        if (fieldSetter == null) {
            throw new RuntimeException("Unsupported type: " + type);
        }
        fieldSetter.setField(field, t, propertyValue);
        validateField(field, t);
    }

    @SneakyThrows
    private void validateField(Field field, Object t) {
        Annotation[] annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
            ConstraintValidator constraintValidator = constraintValidators.get(annotation.annotationType());
            if (constraintValidator != null) {
                field.setAccessible(true);
                if (constraintValidator.supports(annotation, field.getType())) {
                    validateFieldWithConstraintValidator(t, field, annotation, constraintValidator);
                }
            }
        }
    }

    @SneakyThrows
    private void validateFieldWithConstraintValidator(Object t,
                                                      Field field,
                                                      Annotation annotation,
                                                      ConstraintValidator constraintValidator) {
        field.setAccessible(true);
        if (!constraintValidator.isValid(field.get(t), annotation)) {
            Arrays.stream(annotation.annotationType().getMethods())
                    .filter(method -> method.getName().equals("message"))
                    .findFirst()
                    .ifPresentOrElse(methodMessage -> Utility.throwExceptionWithMessage(methodMessage, annotation),
                            () -> Utility.throwExceptionWithoutMessage(annotation));
        }
    }
}
