package ua.org.training.library.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.exceptions.ServiceException;

import java.util.List;

public class ListService<T> {
    private static final Logger LOGGER = LogManager.getLogger(ListService.class);
    public String jsonifyList(List<T> list) throws ServiceException {
        try {
            return new ObjectMapper().writeValueAsString(list);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error while parsing list to JSON", e);
            throw new ServiceException("Error while parsing list to JSON", e);
        }
    }
}
