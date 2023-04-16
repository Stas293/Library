package ua.org.training.library.context;


import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.config.Config;
import ua.org.training.library.context.config.JavaConfig;

@Slf4j
public class ApplicationRunner {
    private ApplicationRunner() {
    }

    public static AnnotationConfigApplicationContext run(Class<?> primarySource) {
        log.info("Starting application");
        log.info("Creating java config");
        String packageName = primarySource.getPackage().getName();
        Config javaConfig = new JavaConfig(packageName);
        log.info("Creating context");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(javaConfig);
        log.info("Creating object factory");
        ObjectFactory objectFactory = new ObjectFactory(context);
        log.info("Setting object factory");
        context.setObjectFactory(objectFactory);
        log.info("Initializing context");
        TomcatServer tomcatServer = new TomcatServer(context);
        ApplicationInitializer initializer = new ApplicationInitializer(context, tomcatServer);
        initializer.initialize();
        log.info("Adding shutdown hook");
        initializer.addShutdownHook();
        log.info("Starting tomcat server");
        ControllerScanner controllerScanner = new ControllerScanner(context);
        log.info("Scanning controllers");
        controllerScanner.scanControllers();
        log.info("Starting server");
        initializer.startServer();
        return context;
    }

}
