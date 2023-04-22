package ua.org.training.library.context;

import ua.org.training.library.context.annotations.ContextInitClass;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        sortedBeans.forEach(context::getObject);
    }

    public List<Class<?>> sortBeans(Set<Class<?>> beans) {
        List<Class<?>> sortedBeans;
        Set<Class<?>> visitedBeans = new HashSet<>();

        sortedBeans = beans.parallelStream()
                .filter(bean -> bean.isAnnotationPresent(ContextInitClass.class))
                .collect(Collectors.toList());

        beans.stream()
                .filter(bean -> !visitedBeans.contains(bean))
                .forEach(bean -> visitBean(bean, visitedBeans, sortedBeans));

        return sortedBeans;
    }

    private void visitBean(Class<?> bean, Set<Class<?>> visitedBeans, List<Class<?>> sortedBeans) {
        visitedBeans.add(bean);

        Constructor<?>[] constructors = bean.getConstructors();
        Arrays.stream(constructors)
                .map(Constructor::getParameterTypes)
                .flatMap(Arrays::stream)
                .filter(parameterType -> !visitedBeans.contains(parameterType)
                        && beans.contains(parameterType))
                .forEach(parameterType -> visitBean(parameterType, visitedBeans, sortedBeans));

        sortedBeans.add(bean);
    }




    public void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(context::close));
    }

    public void startServer() {
        new Thread(tomcatServer).start();
    }
}
