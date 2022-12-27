package ua.org.training.library.web.controller.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.exceptions.ControllerException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.model.Book;
import ua.org.training.library.model.Order;
import ua.org.training.library.model.Status;
import ua.org.training.library.model.User;
import ua.org.training.library.security.SecurityService;
import ua.org.training.library.service.*;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.Links;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.web.controller.ControllerCommand;

import java.util.Date;

public class NewOrder implements ControllerCommand {
    private static final Logger LOGGER = LogManager.getLogger(NewOrder.class);
    private UserService userService = new UserService();
    private OrderService orderService = new OrderService();
    private BookService bookService = new BookService();
    private PlaceService placeService = new PlaceService();
    private StatusService statusService = new StatusService();

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response) {
        try {
            Order newOrder = new Order();
            User author = userService.getUserByLogin(
                    SecurityService.getCurrentLogin(request.getSession())
            );
            Book book = bookService.getBookById(
                    Utility.parseLongOrDefault(request.getParameter(Constants.RequestAttributes.BOOK_ID_ATTRIBUTE),
                            Constants.APP_DEFAULT_ID));
            if (book.getCount() < 1) {
                throw new ControllerException("Book is not available");
            }
            newOrder.setBook(
                    book
            );
            newOrder.setPlace(placeService.getPlaceByName(
                    Utility.parseStringOrDefault(request.getParameter(Constants.RequestAttributes.PLACE_NAME_ATTRIBUTE),
                            Constants.APP_STRING_DEFAULT_VALUE)
            ));
            Status status = statusService.getByCode(
                    Constants.ORDER_DEFAULT_STATUS
            );
            newOrder.setDateCreated(new Date());
            newOrder.setUser(author);
            newOrder.setStatus(status);
            LOGGER.info("New order: " + newOrder);
            long id = orderService.createOrder(newOrder);
            if (id == Constants.INVALID_ID) {
                return Links.USER_PAGE_REDIRECT_NEW_ORDER_NOT_CREATED;
            }
        }  catch (ServiceException | ControllerException e) {
            LOGGER.error(String.format("Error while creating new order: %s", e.getMessage()));
            return Links.USER_PAGE_REDIRECT_NEW_ORDER_NOT_CREATED;
        } finally {
            clearRequestSessionAttributes(request);
        }
        return Links.USER_PAGE_REDIRECT_NEW_ORDER_CREATED;
    }

    @Override
    public void clearRequestSessionAttributes(HttpServletRequest request) {
        request.getSession().removeAttribute(Constants.RequestAttributes.BOOK_ID_ATTRIBUTE);
        request.getSession().removeAttribute(Constants.RequestAttributes.ORDER_DATE_ATTRIBUTE);
        request.getSession().removeAttribute(Constants.RequestAttributes.PLACE_NAME_ATTRIBUTE);
    }
}
