package ua.org.training.library.context;


import jakarta.servlet.Filter;
import jakarta.servlet.Servlet;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.ProtocolHandler;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

import java.io.File;
import java.util.concurrent.Executors;

@Slf4j
@RequiredArgsConstructor
public class TomcatServer implements Runnable {
    private final AnnotationConfigApplicationContext applicationContext;
    private static final String CONTEXT_PATH = "";
    private static final String MAPPING_URL = "/library/*";
    private static final String FILTER_MAPPING_URL = "/*";
    private static final int PORT = 8080;
    private static final String WEB_APP_DIRECTORY = "src/main/webapp";
    private static final String SERVLET_NAME = "dispatcherServlet";


    @SneakyThrows
    public void run() {
        log.debug("Starting tomcat server");
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(PORT);
        tomcat.getConnector();

        Context ctx = tomcat.addWebapp(CONTEXT_PATH,
                new File(WEB_APP_DIRECTORY).getAbsolutePath());

        log.debug("Getting servlet");
        Servlet servlet = applicationContext.getBeansImplementingInterface(Servlet.class)
                .values().stream()
                .findFirst()
                .map(Servlet.class::cast)
                .orElseThrow(() -> new RuntimeException("Dispatcher servlet not found"));

        log.debug("Adding servlet");
        Tomcat.addServlet(ctx, SERVLET_NAME, servlet);
        ctx.addServletMappingDecoded(MAPPING_URL, SERVLET_NAME);

        log.debug("Adding filters");
        addFilters(ctx);

        ProtocolHandler protocolHandler = tomcat.getConnector().getProtocolHandler();
        protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());

        log.info("Starting tomcat server");
        log.info("Listening on port: {}", PORT);

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
            filterMap.addURLPattern(FILTER_MAPPING_URL);
            ctx.addFilterMap(filterMap);
        });
    }
}
