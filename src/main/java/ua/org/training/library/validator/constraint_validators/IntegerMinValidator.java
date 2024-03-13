package ua.org.training.library.validator.constraint_validators;

import jakarta.validation.constraints.Min;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.ContextInitClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Component
@ContextInitClass
public class IntegerMinValidator implements ConstraintValidator<Min, Integer> {
    @Override
    public boolean supports(Annotation annotation, Type type) {
        return type.equals(int.class) || type.equals(Integer.class);
    }

    @Override
    public boolean isValid(Integer value, Min annotation) {
        return value >= annotation.value();
    }
}
