package ua.org.training.library.web.listeners;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.security.SecurityService;

@WebListener
public class SessionListener implements HttpSessionListener {
    private static final Logger LOGGER = LogManager.getLogger(SessionListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        LOGGER.info(String.format("Session %s created", se.getSession().getId()));
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        LOGGER.info(String.format("Session %s destroyed", se.getSession().getId()));
        try {
            SecurityService.removeLoggedUserFromContextAndSession(se.getSession());
        } catch (ServiceException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
