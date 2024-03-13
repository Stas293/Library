package ua.org.training.library.validator.constraint_validators;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public interface ConstraintValidator<A extends Annotation, T> {
    boolean supports(Annotation annotation, Type type);

    boolean isValid(T value, A annotation);
}
