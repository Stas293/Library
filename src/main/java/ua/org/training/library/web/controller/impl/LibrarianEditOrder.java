package ua.org.training.library.web.controller.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.model.*;
import ua.org.training.library.service.*;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.Links;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.web.controller.ControllerCommand;

import java.util.Date;

public class LibrarianEditOrder implements ControllerCommand {
    private static final Logger LOGGER = LogManager.getLogger(LibrarianEditOrder.class);
    private final OrderService orderService = new OrderService();
    private final StatusService statusService = new StatusService();
    private final BookService bookService = new BookService();
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        long id = Utility.parseLongOrDefault(request.getParameter(Constants.RequestAttributes.ORDER_ID_ATTRIBUTE),
                Constants.APP_DEFAULT_ID);
        try {
            Order order = orderService.getOrderById(id);
            setDate(request, order);
            Status status = statusService.getByCode(
                    Utility.getStringParameter(request.getParameter(Constants.RequestAttributes.ORDER_STATUS_ATTRIBUTE),
                    Constants.STATUS_CODE_REGISTERED));
            order.setStatus(status);
            LOGGER.info("Order updated: " + order);
            LOGGER.info("Order updated: " + order.getDateExpire());
            if (!status.isClosed()) {
                orderService.updateOrder(order);
            } else {
                HistoryOrder historyOrder = HistoryOrder.builder()
                        .setUser(order.getUser())
                        .setBookName(order.getBook().getName())
                        .setDateCreated(order.getDateCreated())
                        .setDateExpire(new Date())
                        .setStatus(order.getStatus())
                        .createHistoryOrder();
                orderService.deleteOrder(order.getId());
                Book book = order.getBook();
                book.setCount(book.getCount() + 1);
                bookService.updateBook(book);
                HistoryOrderService historyOrderService = new HistoryOrderService();
                historyOrderService.createHistoryOrder(historyOrder);
            }
        } catch (ServiceException e) {
            LOGGER.error("Error while getting order by id", e);
        }
        clearRequestSessionAttributes(request);
        return Links.LIBRARIAN_ORDER_UPDATE_SUCCESS;
    }

    private static void setDate(HttpServletRequest request, Order order) {
        try {
            Date date = Utility.parseDateOrDefault(request.getParameter(Constants.RequestAttributes.ORDER_DATE_ATTRIBUTE),
                    new Date(new Date().getTime() + 86400000));
            LOGGER.info("date: " + request.getParameter(Constants.RequestAttributes.ORDER_DATE_ATTRIBUTE));
            order.setDateExpire(date);
        } catch (NullPointerException e) {
            LOGGER.error("Date is null");
        }
    }

    @Override
    public void clearRequestSessionAttributes(HttpServletRequest request) {
        request.getSession().removeAttribute(Constants.RequestAttributes.ORDER_ID_ATTRIBUTE);
        request.getSession().removeAttribute(Constants.RequestAttributes.ORDER_DATE_ATTRIBUTE);
        request.getSession().removeAttribute(Constants.RequestAttributes.APP_LOGIN_ATTRIBUTE);
        request.getSession().removeAttribute(Constants.RequestAttributes.BOOK_ID_ATTRIBUTE);
    }
}
