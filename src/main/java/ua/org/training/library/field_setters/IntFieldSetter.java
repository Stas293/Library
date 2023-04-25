package ua.org.training.library.field_setters;

import ua.org.training.library.context.annotations.ClassManagerType;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.ContextInitClass;

import java.lang.reflect.Field;

@Component
@ContextInitClass
@ClassManagerType(values = {int.class, Integer.class})
public class IntFieldSetter implements FieldSetter {
    @Override
    public void setField(Field field, Object obj, String value) throws IllegalAccessException {
        field.setAccessible(true);
        field.setInt(obj, Integer.parseInt(value));
        field.setAccessible(false);
    }
}
