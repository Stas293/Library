package ua.org.training.library.context.proxy;


import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import ua.org.training.library.connections.TransactionManager;
import ua.org.training.library.context.AnnotationConfigApplicationContext;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.Service;
import ua.org.training.library.context.annotations.Transactional;
import ua.org.training.library.exceptions.DaoException;
import ua.org.training.library.exceptions.ServiceException;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TransactionalHandlerProxyConfigurator implements ProxyConfigurator {
    @Override
    public <T> T getProxyObjectWhenNeeded(T object, Class<?> implClass, AnnotationConfigApplicationContext context) {
        if (!implClass.isAnnotationPresent(Service.class)) {
            return object;
        }
        Set<String> transactionalMethods = getTransactionalMethods(implClass);
        log.info("Creating proxy for class: {}", implClass.getName());
        if (implClass.getInterfaces().length > 0) {
            return getRegularProxyIfClassImplementsInterfaces(object, implClass, transactionalMethods, context);
        }
        return getProxyWithJavassist(object, transactionalMethods, context);
    }

    @SneakyThrows
    private <T> T getProxyWithJavassist(T object, Set<String> transactionalMethods, AnnotationConfigApplicationContext context) {
        ProxyFactory factory = new ProxyFactory();
        factory.setSuperclass(object.getClass());
        Constructor<?> constructorArgs = object.getClass().getConstructors()[0];
        Object[] constructorObjects = getConstructorObjects(object, constructorArgs);
        TransactionManager finalTransactionManager = context.getObject(TransactionManager.class);
        MethodHandler methodHandler = (self, thisMethod, proceed, args) ->
                checkIfMethodIsTransactionalAndInvokeMethod(object,
                        transactionalMethods, finalTransactionManager, thisMethod, args);
        return (T) factory.create(constructorArgs.getParameterTypes(), constructorObjects, methodHandler);
    }

    private Object checkIfMethodIsTransactionalAndInvokeMethod(Object object, Set<String> transactionalMethods,
                                                               TransactionManager finalTransactionManager,
                                                               Method thisMethod, Object[] args)
            throws IllegalAccessException, InvocationTargetException {
        assert finalTransactionManager != null;
        if (transactionalMethods.contains(thisMethod.getName())) {
            log.info("Creating transactional method invocation for method: {}", thisMethod.getName());
            log.info("This method: {}", thisMethod);
            return createTransactionalMethodInvocation(object, thisMethod, finalTransactionManager, args);
        } else {
            return invokeMethodAndCloseConnection(object, finalTransactionManager, thisMethod, args);
        }
    }

    @SneakyThrows
    private Object invokeMethodAndCloseConnection(Object object, TransactionManager finalTransactionManager, Method thisMethod, Object[] args) throws IllegalAccessException, InvocationTargetException {
        if (methodFromObjectClass(thisMethod)) {
            return thisMethod.invoke(object, args);
        }
        log.info("Creating regular method invocation for method: {}", thisMethod.getName());
        log.info("This method: {}", thisMethod);
        try {
            return thisMethod.invoke(object, args);
        } catch (DaoException e) {
            log.error("Dao exception: {}", e.getMessage());
            throw new ServiceException(e.getMessage());
        } finally {
            finalTransactionManager.closeConnection();
        }
    }

    private boolean methodFromObjectClass(Method thisMethod) {
        Set<String> methods = Arrays.stream(Object.class.getMethods())
                .map(Method::getName)
                .collect(Collectors.toSet());
        return methods.contains(thisMethod.getName());
    }

    @NotNull
    private Object[] getConstructorObjects(Object object, Constructor<?> constructorArgs) {
        Field[] fields = object.getClass().getDeclaredFields();
        return Arrays.stream(fields)
                .map(field -> getObjectFromField(object, field))
                .filter(getObjectsFromObjectForConstructor(constructorArgs))
                .toArray();
    }

    @SneakyThrows
    private Object getObjectFromField(Object object, Field field) {
        try {
            field.setAccessible(true);
            return field.get(object);
        } finally {
            field.setAccessible(false);
        }
    }

    @NotNull
    private Predicate<Object> getObjectsFromObjectForConstructor(Constructor<?> constructorArgs) {
        return field -> constructorArgs.getParameterTypes().length == 0
                || Arrays.stream(constructorArgs.getParameterTypes())
                .anyMatch(type -> type.isAssignableFrom(field.getClass()));
    }

    @NotNull
    private <T> T getRegularProxyIfClassImplementsInterfaces(T object, Class<?> implClass,
                                                             Set<String> transactionalMethods, AnnotationConfigApplicationContext context) {
        log.info("Creating regular proxy for class: {}", implClass.getName());
        TransactionManager finalTransactionManager = context.getObject(TransactionManager.class);
        return (T) Proxy.newProxyInstance(implClass.getClassLoader(),
                implClass.getInterfaces(), (proxy, method, args) ->
                        checkIfMethodIsTransactionalAndInvokeMethod(object,
                                transactionalMethods, finalTransactionManager, method, args));
    }

    @NotNull
    private Set<String> getTransactionalMethods(Class<?> implClass) {
        return Arrays.stream(implClass.getMethods())
                .filter(method -> method.isAnnotationPresent(Transactional.class))
                .map(Method::getName)
                .collect(Collectors.toSet());
    }

    private Object createTransactionalMethodInvocation(Object object, Method method,
                                                              TransactionManager transactionManager,
                                                              Object... args) {
        try {
            transactionManager.beginTransaction();
            Object retVal = method.invoke(object, args);
            transactionManager.commitTransaction();
            return retVal;
        } catch (IllegalAccessException | InvocationTargetException | DaoException e) {
            transactionManager.rollbackTransaction();
            throw new ServiceException("Error in transactional method invocation: " + method.getName(), e);
        }
    }
}
