package ua.org.training.library.validator.constraint_validators;


import jakarta.validation.constraints.NotBlank;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.ContextInitClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Component
@ContextInitClass
public class NotBlankValidator implements ConstraintValidator<NotBlank, String> {
    @Override
    public boolean supports(Annotation annotation, Type type) {
        return type.equals(String.class);
    }

    @Override
    public boolean isValid(String value, NotBlank annotation) {
        return value != null && !value.trim().isEmpty();
    }
}
