package ua.org.training.library.web.controller.impl.librarian;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.context.ApplicationContext;
import ua.org.training.library.exceptions.ConnectionDBException;
import ua.org.training.library.exceptions.ControllerException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.model.*;
import ua.org.training.library.service.*;
import ua.org.training.library.utility.*;
import ua.org.training.library.utility.validation.OrderValidation;
import ua.org.training.library.web.controller.ControllerCommand;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public class LibrarianEditOrder implements ControllerCommand {
    private static final Logger LOGGER = LogManager.getLogger(LibrarianEditOrder.class);
    private final OrderService orderService = ApplicationContext.getInstance().getOrderService();
    private final StatusService statusService = ApplicationContext.getInstance().getStatusService();
    private final BookService bookService = ApplicationContext.getInstance().getBookService();
    private final HistoryOrderService historyOrderService = ApplicationContext.getInstance().getHistoryOrderService();
    private final MailService mailService = ApplicationContext.getInstance().getMailService();

    private void setDate(HttpServletRequest request, Order order) {
        Date date = Utility.parseDateOrDefault(
                request.getParameter(Constants.RequestAttributes.ORDER_DATE_ATTRIBUTE),
                Date.from(LocalDate
                        .now()
                        .plusDays(1)
                        .atStartOfDay()
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                )
        );
        LOGGER.info(String.format("date: %s", date));
        if (!OrderValidation.isDateExpireValid(date)) {
            throw new ControllerException("Date is not valid");
        }
        order.setDateExpire(date);
    }

    private void sendEmail(HttpServletRequest request, Order order) {
        mailService.sendOrderMail(Utility.getLocale(request), order);
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info(request.getMethod());
        long id = Utility.parseLongOrDefault(
                request.getParameter(Constants.RequestAttributes.ORDER_ID_ATTRIBUTE),
                Constants.APP_DEFAULT_ID);
        try {
            Order order = orderService.getOrderById(id);
            Status status = statusService.getByCode(
                    Utility.getStringParameter(
                            request.getParameter(Constants.RequestAttributes.ORDER_STATUS_ATTRIBUTE),
                            Constants.STATUS_CODE_REGISTERED));
            order.setStatus(status);
            LOGGER.info(String.format("order: %s", order));
            if (!status.isClosed()) {
                setDate(request, order);
                sendEmail(request, order);
                orderService.updateOrder(order);
            } else {
                sendEmail(request, order);
                HistoryOrder historyOrder = Mapper.orderToHistoryOrder(order);
                orderService.deleteOrder(order.getId());
                Book book = order.getBook();
                book.setCount(book.getCount() + 1);
                bookService.updateBook(book);
                historyOrderService.createHistoryOrder(historyOrder);
            }
        } catch (ServiceException | ControllerException e) {
            LOGGER.error("Error while getting order by id", e);
            return Links.LIBRARIAN_ORDER_UPDATE_FAILED;
        } catch (ConnectionDBException e) {
            LOGGER.error(e.getMessage(), e);
            return Links.ERROR_PAGE + "?message=" + e.getMessage();
        }
        return Links.LIBRARIAN_ORDER_UPDATE_SUCCESS;
    }
}
