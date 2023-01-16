package ua.org.training.library.web.listeners;

import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.context.ApplicationContext;

@WebListener
public class ContextListener implements ServletContextListener {
    private static final Logger LOGGER = LogManager.getLogger(ContextListener.class);

    @Override
    public void contextInitialized(jakarta.servlet.ServletContextEvent sce) {
        LOGGER.info("Context initialized");
        ApplicationContext.getInstance();
    }
}
