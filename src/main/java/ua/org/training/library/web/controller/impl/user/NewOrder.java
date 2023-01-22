package ua.org.training.library.web.controller.impl.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.context.ApplicationContext;
import ua.org.training.library.exceptions.ConnectionDBException;
import ua.org.training.library.exceptions.ControllerException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.model.*;
import ua.org.training.library.security.SecurityService;
import ua.org.training.library.service.*;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.Links;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.web.controller.ControllerCommand;

import java.util.Date;

public class NewOrder implements ControllerCommand {
    private static final Logger LOGGER = LogManager.getLogger(NewOrder.class);
    private final UserService userService = ApplicationContext.getInstance().getUserService();
    private final OrderService orderService = ApplicationContext.getInstance().getOrderService();
    private final BookService bookService = ApplicationContext.getInstance().getBookService();
    private final PlaceService placeService = ApplicationContext.getInstance().getPlaceService();
    private final StatusService statusService = ApplicationContext.getInstance().getStatusService();
    private final MailService mailService = ApplicationContext.getInstance().getMailService();

    @Override
    public String execute(HttpServletRequest request,
                          HttpServletResponse response) {
        try {
            Order newOrder = new Order();
            User author = userService.getUserByLogin(
                    SecurityService.getCurrentLogin(request.getSession())
            );
            Book book = bookService.getBookById(
                    Utility.parseLongOrDefault(
                            request.getParameter(Constants.RequestAttributes.BOOK_ID_ATTRIBUTE),
                            Constants.APP_DEFAULT_ID));
            if (book.getCount() < 1) {
                throw new ControllerException("Book is not available");
            }
            Place place = placeService.getPlaceByName(
                    Utility.parseStringOrDefault(request.getParameter(Constants.RequestAttributes.PLACE_NAME_ATTRIBUTE),
                            Constants.APP_STRING_DEFAULT_VALUE));
            Status status = statusService.getByCode(
                    Constants.ORDER_DEFAULT_STATUS
            );
            newOrder.setBook(book);
            newOrder.setPlace(place);
            newOrder.setDateCreated(new Date());
            newOrder.setUser(author);
            newOrder.setStatus(status);
            LOGGER.info(String.format("New order created: %s", newOrder));
            mailService.sendOrderMail(Utility.getLocale(request), newOrder);
            long id = orderService.createOrder(newOrder);
            if (id == Constants.INVALID_ID) {
                return Links.USER_PAGE_REDIRECT_NEW_ORDER_NOT_CREATED;
            }
        } catch (ServiceException | ControllerException e) {
            LOGGER.error(String.format("Error while creating new order: %s", e.getMessage()));
            return Links.USER_PAGE_REDIRECT_NEW_ORDER_NOT_CREATED;
        } catch (ConnectionDBException e) {
            LOGGER.error(String.format("Error while creating new order: %s", e.getMessage()));
            return Links.ERROR_PAGE;
        }
        return Links.USER_PAGE_REDIRECT_NEW_ORDER_CREATED;
    }
}
