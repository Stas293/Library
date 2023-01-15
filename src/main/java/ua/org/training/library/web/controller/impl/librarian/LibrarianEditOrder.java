package ua.org.training.library.web.controller.impl.librarian;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.context.ApplicationContext;
import ua.org.training.library.exceptions.ConnectionDBException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.model.*;
import ua.org.training.library.service.*;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.Links;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.web.controller.ControllerCommand;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class LibrarianEditOrder implements ControllerCommand {
    private static final Logger LOGGER = LogManager.getLogger(LibrarianEditOrder.class);
    private final OrderService orderService = ApplicationContext.getInstance().getOrderService();
    private final StatusService statusService = ApplicationContext.getInstance().getStatusService();
    private final BookService bookService = ApplicationContext.getInstance().getBookService();
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        long id = Utility.parseLongOrDefault(request.getParameter(Constants.RequestAttributes.ORDER_ID_ATTRIBUTE),
                Constants.APP_DEFAULT_ID);
        try {
            Order order = orderService.getOrderById(id);
            Status status = statusService.getByCode(
                    Utility.getStringParameter(request.getParameter(Constants.RequestAttributes.ORDER_STATUS_ATTRIBUTE),
                            Constants.STATUS_CODE_REGISTERED));
            setDate(request, order);
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
                HistoryOrderService historyOrderService = ApplicationContext.getInstance().getHistoryOrderService();
                historyOrderService.createHistoryOrder(historyOrder);
            }
        } catch (ServiceException e) {
            LOGGER.error("Error while getting order by id", e);
            return Links.LIBRARIAN_ORDER_UPDATE_FAILED;
        } catch (ConnectionDBException e) {
            LOGGER.error(e.getMessage(), e);
            return Links.ERROR_PAGE + "?message=" + e.getMessage();
        }
        return Links.LIBRARIAN_ORDER_UPDATE_SUCCESS;
    }

    private static void setDate(HttpServletRequest request, Order order) {
        try {
            Date date = Utility.parseDateOrDefault(request.getParameter(Constants.RequestAttributes.ORDER_DATE_ATTRIBUTE),
                    Date.from(LocalDate.now().plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            LOGGER.info("date: " + request.getParameter(Constants.RequestAttributes.ORDER_DATE_ATTRIBUTE));
            order.setDateExpire(date);
        } catch (NullPointerException e) {
            LOGGER.error("Date is null");
        }
    }
}
