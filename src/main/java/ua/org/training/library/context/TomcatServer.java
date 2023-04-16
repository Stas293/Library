package ua.org.training.library.context;


import jakarta.servlet.Filter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import ua.org.training.library.web.DispatcherServlet;

import java.io.File;

@Slf4j
public class TomcatServer implements Runnable {
    private final AnnotationConfigApplicationContext applicationContext;
    private static final String CONTEXT_PATH = "";
    private static final String MAPPING_URL = "/library/*";
    private static final int PORT = 8080;
    private static final String WEB_APP_DIRECTORY = "src/main/webapp";

    public TomcatServer(AnnotationConfigApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    @SneakyThrows
    public void run() {
        log.debug("Starting tomcat server");
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir("temp");
        tomcat.setPort(PORT);
        tomcat.getConnector();

        Context ctx = tomcat.addWebapp(CONTEXT_PATH, new File(WEB_APP_DIRECTORY).getAbsolutePath());

        log.debug("Getting servlet");
        DispatcherServlet servlet = applicationContext.getObject(DispatcherServlet.class);

        log.debug("Adding servlet");
        Tomcat.addServlet(ctx, "my-servlet", servlet);
        ctx.addServletMappingDecoded(MAPPING_URL, "my-servlet");

        log.debug("Adding filters");
        addFilters(ctx);

        // Start the server
        tomcat.start();
        tomcat.getServer().await();
    }

    private void addFilters(Context ctx) {
        applicationContext.getBeansImplementingInterface(Filter.class).forEach((aClass, filter) -> {
            log.debug("Adding filter: {}", aClass);
            log.debug("Filter class: {}", filter.getClass().getName());
            FilterDef filterDef = new FilterDef();
            filterDef.setFilter(filter);
            filterDef.setFilterName(aClass.getName());
            ctx.addFilterDef(filterDef);
            FilterMap filterMap = new FilterMap();
            filterMap.setFilterName(aClass.getName());
            filterMap.addURLPattern("/*");
            ctx.addFilterMap(filterMap);
        });
    }
}
