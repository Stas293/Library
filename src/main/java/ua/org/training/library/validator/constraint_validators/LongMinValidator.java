package ua.org.training.library.validator.constraint_validators;

import jakarta.validation.constraints.Min;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.ContextInitClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Component
@ContextInitClass
public class LongMinValidator implements ConstraintValidator<Min, Long> {
    @Override
    public boolean supports(Annotation annotation, Type type) {
        return type.equals(long.class) || type.equals(Long.class);
    }

    @Override
    public boolean isValid(Long value, Min annotation) {
        return value >= annotation.value();
    }
}
