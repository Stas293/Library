package ua.org.training.library.context;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.dao.DaoFactory;
import ua.org.training.library.service.*;
import ua.org.training.library.utility.CaptchaValidator;

import java.util.concurrent.atomic.AtomicReference;

public class ApplicationContext {
    private static final Logger LOGGER = LogManager.getLogger(ApplicationContext.class);
    private static AtomicReference<ApplicationContext> instance;
    private final AuthorService authorService;
    private final BookService bookService;
    private final HistoryOrderService historyOrderService;
    private final OrderService orderService;
    private final RoleService roleService;
    private final UserService userService;
    private final StatusService statusService;
    private final PlaceService placeService;
    private final CaptchaValidator captchaValidator;

    private ApplicationContext() {
        DaoFactory daoFactory = DaoFactory.getInstance();
        authorService = new AuthorService(daoFactory);
        bookService = new BookService(daoFactory);
        historyOrderService = new HistoryOrderService(daoFactory);
        orderService = new OrderService(daoFactory);
        roleService = new RoleService(daoFactory);
        userService = new UserService(daoFactory);
        statusService = new StatusService(daoFactory);
        placeService = new PlaceService(daoFactory);
        captchaValidator = new CaptchaValidator();
        LOGGER.info("Application context initialized");
    }

    public static ApplicationContext getInstance() {
        if (instance == null) {
            instance = new AtomicReference<>(new ApplicationContext());
        }
        return instance.get();
    }

    public AuthorService getAuthorService() {
        return authorService;
    }

    public BookService getBookService() {
        return bookService;
    }

    public HistoryOrderService getHistoryOrderService() {
        return historyOrderService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public RoleService getRoleService() {
        return roleService;
    }

    public UserService getUserService() {
        return userService;
    }

    public CaptchaValidator getCaptchaValidator() {
        return captchaValidator;
    }

    public StatusService getStatusService() {
        return statusService;
    }

    public PlaceService getPlaceService() {
        return placeService;
    }
}
