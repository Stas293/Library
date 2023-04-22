package ua.org.training.library.field_setters;

import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.ContextInitClass;
import ua.org.training.library.context.annotations.FieldSetterType;

import java.lang.reflect.Field;

@Component
@ContextInitClass
@FieldSetterType(values = {boolean.class, Boolean.class})
public class BooleanFieldSetter implements FieldSetter {
    @Override
    public void setField(Field field, Object obj, String value) throws IllegalAccessException {
        field.setAccessible(true);
        field.setBoolean(obj, Boolean.parseBoolean(value));
        field.setAccessible(false);
    }
}
