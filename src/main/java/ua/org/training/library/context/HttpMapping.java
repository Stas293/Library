package ua.org.training.library.context;

import java.io.Serializable;
import java.lang.reflect.Method;

public record HttpMapping(String httpMethod, String httpPath, Method method, Object controller) implements Serializable {

}
