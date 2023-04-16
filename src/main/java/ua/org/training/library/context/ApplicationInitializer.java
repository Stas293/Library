package ua.org.training.library.context;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ApplicationInitializer {
    private final AnnotationConfigApplicationContext context;
    private final TomcatServer tomcatServer;
    private final Set<Class<?>> beans;

    public ApplicationInitializer(AnnotationConfigApplicationContext context,
                                  TomcatServer tomcatServer) {
        this.context = context;
        this.beans = context.getConfig().getReflectionsSet();
        this.tomcatServer = tomcatServer;
    }

    public void initialize() {
        List<Class<?>> sortedBeans = sortBeans(beans);
        for (Class<?> bean : sortedBeans) {
            context.getObject(bean);
        }
    }

    public List<Class<?>> sortBeans(Set<Class<?>> beans) {
        List<Class<?>> sortedBeans = new ArrayList<>();
        Set<Class<?>> visitedBeans = new HashSet<>();

        for (Class<?> bean : beans) {
            if (!visitedBeans.contains(bean)) {
                visitBean(bean, visitedBeans, sortedBeans);
            }
        }

        return sortedBeans;
    }

    private void visitBean(Class<?> bean, Set<Class<?>> visitedBeans, List<Class<?>> sortedBeans) {
        visitedBeans.add(bean);

        Constructor<?>[] constructors = bean.getConstructors();
        for (Constructor<?> constructor : constructors) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            for (Class<?> parameterType : parameterTypes) {
                if (!visitedBeans.contains(parameterType) && beans.contains(parameterType)) {
                    visitBean(parameterType, visitedBeans, sortedBeans);
                }
            }
        }

        sortedBeans.add(bean);
    }




    public void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(context::close));
    }

    public void startServer() {
        new Thread(tomcatServer).start();
    }
}
