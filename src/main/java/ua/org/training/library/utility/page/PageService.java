package ua.org.training.library.utility.page;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PageService<T> {
    private static final Long DEFAULT_PAGE_NUMBER = 0L;
    private static final Long DEFAULT_PAGE_SIZE = 5L;
    private static final String PAGE_NUMBER_ATTRIBUTE = "page";
    private static final String PAGE_LIMIT_ATTRIBUTE = "limit";
    private static final String SEARCH_ATTRIBUTE = "search";
    private static final String SORTING_ATTRIBUTE = "sorting";
    private static final Logger LOGGER = LogManager.getLogger(PageService.class);

    public String jsonifyPage(Page<T> page) throws ServiceException {
        try {
            return new ObjectMapper().writeValueAsString(createMap(page));
        } catch (JsonProcessingException e) {
            LOGGER.error("Error while parsing page to JSON", e);
            throw new ServiceException("Error while parsing page to JSON", e);
        }
    }

    private Map<String, Object> createMap(Page<T> page) {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("content", !page.getData().isEmpty() ? page.getData() : new ArrayList<>());
        objectMap.put("limit", page.getLimit());
        objectMap.put("elementsCount", page.getElementsCount());

        return objectMap;
    }

    public Page<T> getPage(HttpServletRequest request) {
        return Page.<T>builder()
                .setPageNumber(
                        Utility.parseLongOrDefault(
                                request.getParameter(PAGE_NUMBER_ATTRIBUTE),
                                DEFAULT_PAGE_NUMBER))
                .setLimit(
                        Utility.parseLongOrDefault(
                                request.getParameter(PAGE_LIMIT_ATTRIBUTE),
                                DEFAULT_PAGE_SIZE))
                .setSearch(
                        Utility.parseStringOrDefault(
                                request.getParameter(SEARCH_ATTRIBUTE),
                                Constants.APP_STRING_DEFAULT_VALUE))
                .setSorting(
                        Utility.parseStringOrDefault(
                                request.getParameter(SORTING_ATTRIBUTE),
                                Constants.APP_STRING_DEFAULT_VALUE))
                .createPage();
    }
}
