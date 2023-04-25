package ua.org.training.library.field_setters;

import ua.org.training.library.context.annotations.ClassManagerType;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.ContextInitClass;

import java.lang.reflect.Field;

@Component
@ContextInitClass
@ClassManagerType(values = {double.class, Double.class})
public class DoubleFieldSetter implements FieldSetter {
    @Override
    public void setField(Field field, Object obj, String value) throws IllegalAccessException {
        field.setAccessible(true);
        field.setDouble(obj, Double.parseDouble(value));
        field.setAccessible(false);
    }
}
