package ua.org.training.library.field_setters;

import ua.org.training.library.context.annotations.ClassManagerType;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.ContextInitClass;

import java.lang.reflect.Field;

@Component
@ContextInitClass
@ClassManagerType(values = {long.class, Long.class})
public class LongFieldSetter implements FieldSetter {
    @Override
    public void setField(Field field, Object obj, String value) throws IllegalAccessException {
        field.setAccessible(true);
        field.setLong(obj, Long.parseLong(value));
        field.setAccessible(false);
    }
}
