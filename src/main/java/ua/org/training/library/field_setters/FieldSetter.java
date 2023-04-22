package ua.org.training.library.field_setters;

import java.lang.reflect.Field;

public interface FieldSetter {
    void setField(Field field, Object obj, String value) throws IllegalAccessException;
}
