package ua.org.training.library.context;

import java.io.Serializable;
import java.lang.invoke.MethodHandle;

public record HttpMapping(String httpMethod,
                          String httpPath,
                          MethodHandle method,
                          Object controller) implements Serializable {
}
