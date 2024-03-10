package ua.org.training.library.repository.impl;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.connections.TransactionManager;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.dao.*;
import ua.org.training.library.model.*;
import ua.org.training.library.repository.OrderRepository;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.Sort;

import java.sql.Connection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Component
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OrderRepositoryImpl implements OrderRepository {
    private final OrderDao orderDao;
    private final TransactionManager transactionManager;
    private final BookDao bookDao;
    private final StatusDao statusDao;
    private final UserDao userDao;
    private final PlaceDao placeDao;
    private final PlaceNameDao placeNameDao;
    private final StatusNameDao statusNameDao;

    @Override
    public Page<Order> getPageByBook(Pageable page, Book book) {
        log.info("Getting page of orders by book: {}", book);
        Connection conn = transactionManager.getConnection();
        return orderDao.getPageByBookId(conn, page, book.getId());
    }

    @Override
    public Page<Order> getPageByStatusAndUser(Pageable page, Status status, User user, Locale locale) {
        log.info("Getting page of orders by status: {} and user: {}", status, user);
        Connection conn = transactionManager.getConnection();
        Page<Order> orderPage = orderDao.getPageByStatusAndUserId(conn,
                page,
                status.getId(),
                user.getId());
        orderPage.getContent().forEach(
                order -> {
                    order.setBook(bookDao.getBookByOrderId(conn, order.getId()).orElse(null));
                    order.setPlace(placeDao.getByOrderId(conn, order.getId()).orElse(null));
                    order.getPlace().setNames(
                            List.of(
                                    placeNameDao.getByPlaceId(conn, order.getPlace().getId(), locale).orElseThrow()
                            )
                    );
                }
        );
        return orderPage;
    }

    @Override
    public Page<Order> getPageByStatus(Pageable page, Status status) {
        log.info("Getting page of orders by status: {}", status);
        Connection conn = transactionManager.getConnection();
        Page<Order> pageByStatus = orderDao.getPageByStatusId(conn, page, status.getId());
        pageByStatus.getContent().forEach(
                order -> {
                    order.setBook(bookDao.getBookByOrderId(conn, order.getId()).orElse(null));
                    order.setUser(userDao.getByOrderId(conn, order.getId()).orElse(null));
                }
        );
        return pageByStatus;
    }

    @Override
    public Page<Order> getPageByStatusAndUserAndSearch(Pageable page, Status statusOrder, User user,
                                                       String search, Locale locale) {
        log.info("Getting page of orders by status: {} and user: {} and search: {}", statusOrder, user, search);
        Connection conn = transactionManager.getConnection();
        Page<Order> orderPage = orderDao.getPageByStatusAndUserAndSearch(conn,
                page,
                statusOrder.getId(),
                user.getId(),
                search);
        orderPage.getContent().forEach(
                order -> {
                    order.setBook(bookDao.getBookByOrderId(conn, order.getId()).orElse(null));
                    order.setPlace(placeDao.getByOrderId(conn, order.getId()).orElse(null));
                    order.getPlace().setNames(
                            List.of(
                                    placeNameDao.getByPlaceId(conn, order.getPlace().getId(), locale).orElseThrow()
                            )
                    );
                }
        );
        return orderPage;
    }

    @Override
    public Order save(Order entity) {
        log.info("Saving order: {}", entity);
        Connection conn = transactionManager.getConnection();
        if (entity.getId() == null) {
            entity = orderDao.create(conn, entity);
        } else {
            orderDao.update(conn, entity);
        }
        return entity;
    }

    @Override
    public List<Order> saveAll(List<Order> entities) {
        log.info("Saving orders: {}", entities);
        Connection conn = transactionManager.getConnection();
        List<Order> ordersToSave = entities.stream()
                .filter(order -> order.getId() == null)
                .toList();
        List<Order> ordersToUpdate = entities.stream()
                .filter(order -> order.getId() != null)
                .toList();
        if (!ordersToSave.isEmpty()) {
            ordersToSave = orderDao.create(conn, ordersToSave);
        }
        if (!ordersToUpdate.isEmpty()) {
            orderDao.update(conn, ordersToUpdate);
        }
        ordersToSave.addAll(ordersToUpdate);
        log.info("Setting book and status to orders: {}", ordersToSave);
        for (Order order : ordersToSave) {
            order.setBook(bookDao.getBookByOrderId(conn, order.getId()).orElse(null));
            order.setStatus(statusDao.getByOrderId(conn, order.getStatus().getId()).orElse(null));
            order.setUser(userDao.getByOrderId(conn, order.getUser().getId()).orElse(null));
            order.setPlace(placeDao.getByOrderId(conn, order.getPlace().getId()).orElse(null));
        }
        return ordersToSave;
    }

    @Override
    public Optional<Order> findById(Long aLong) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Order> findById(Long aLong, Locale locale) {
        log.info("Getting order by id: {}", aLong);
        Connection conn = transactionManager.getConnection();
        Optional<Order> order = orderDao.getById(conn, aLong);
        log.info("Setting book and status to order: {}", order);
        if (order.isPresent()) {
            order.get().setBook(bookDao.getBookByOrderId(conn, order.get().getId()).orElseThrow());
            order.get().setStatus(statusDao.getByOrderId(conn, order.get().getId()).orElseThrow());
            order.get().setUser(userDao.getByOrderId(conn, order.get().getId()).orElseThrow());
            order.get().getStatus().setNames(
                    List.of(
                            statusNameDao.getByStatusId(conn, order.get().getStatus().getId(), locale).orElseThrow()
                    )
            );
            order.get().setPlace(placeDao.getByOrderId(conn, order.get().getId()).orElseThrow());
            order.get().getPlace().setNames(
                    List.of(
                            placeNameDao.getByPlaceId(conn, order.get().getPlace().getId(), locale).orElseThrow()
                    )
            );
        }
        return order;
    }

    @Override
    public Page<Order> getPageByStatusAndSearch(Pageable page, Status statusOrder, String search) {
        log.info("Getting page of orders by status: {} and search: {}", statusOrder, search);
        Connection conn = transactionManager.getConnection();
        Page<Order> orderPage = orderDao.getPageByStatusAndSearch(conn, page, statusOrder.getId(), search);
        orderPage.getContent().forEach(
                order -> {
                    order.setBook(bookDao.getBookByOrderId(conn, order.getId()).orElse(null));
                    order.setUser(userDao.getByOrderId(conn, order.getId()).orElse(null));
                }
        );
        return orderPage;
    }

    @Override
    public Page<Order> getPageByStatusAndPlaceAndSearch(Pageable page, Status statusOrder,
                                                        Place placeOrder, String search) {
        log.info("Getting page of orders by status: {} and place: {} and search: {}", statusOrder, placeOrder, search);
        Connection conn = transactionManager.getConnection();
        Page<Order> orderPage = orderDao.getPageByStatusAndPlaceAndSearch(conn,
                page,
                statusOrder.getId(),
                placeOrder.getId(),
                search);
        orderPage.getContent().forEach(
                order -> {
                    order.setBook(bookDao.getBookByOrderId(conn, order.getId()).orElse(null));
                    order.setUser(userDao.getByOrderId(conn, order.getId()).orElse(null));
                }
        );
        return orderPage;
    }

    @Override
    public Page<Order> getPageByStatusAndPlace(Pageable page, Status statusOrder, Place placeOrder) {
        log.info("Getting page of orders by status: {} and place: {}", statusOrder, placeOrder);
        Connection conn = transactionManager.getConnection();
        Page<Order> orderPage = orderDao.getPageByStatusAndPlace(conn,
                page,
                statusOrder.getId(),
                placeOrder.getId());
        orderPage.getContent().forEach(
                order -> {
                    order.setBook(bookDao.getBookByOrderId(conn, order.getId()).orElse(null));
                    order.setUser(userDao.getByOrderId(conn, order.getId()).orElse(null));
                }
        );
        return orderPage;
    }

    @Override
    public Optional<Order> findOrderByUserIdAndBookId(Long authorityUserId, long bookId) {
        log.info("Getting order by user id: {} and book id: {}", authorityUserId, bookId);
        Connection conn = transactionManager.getConnection();
        return orderDao.getOrderByUserIdAndBookId(conn, authorityUserId, bookId);
    }

    @Override
    public List<Order> findOrdersByBookId(long id) {
        log.info("Getting orders by book id: {}", id);
        Connection conn = transactionManager.getConnection();
        return orderDao.getOrdersByBookId(conn, id);
    }

    @Override
    public boolean existsById(Long aLong) {
        log.info("Checking if order exists by id: {}", aLong);
        Connection conn = transactionManager.getConnection();
        return orderDao.getById(conn, aLong).isPresent();
    }

    @Override
    public List<Order> findAll() {
        log.info("Getting all orders");
        Connection conn = transactionManager.getConnection();
        return orderDao.getAll(conn);
    }

    @Override
    public List<Order> findAllById(List<Long> longs) {
        log.info("Getting orders by ids: {}", longs);
        Connection conn = transactionManager.getConnection();
        return orderDao.getByIds(conn, longs);
    }

    @Override
    public long count() {
        log.info("Counting orders");
        Connection conn = transactionManager.getConnection();
        return orderDao.count(conn);
    }

    @Override
    public void deleteById(Long aLong) {
        log.info("Deleting order by id: {}", aLong);
        Connection conn = transactionManager.getConnection();
        orderDao.delete(conn, aLong);
    }

    @Override
    public void delete(Order entity) {
        log.info("Deleting order: {}", entity);
        Connection conn = transactionManager.getConnection();
        orderDao.delete(conn, entity.getId());
    }

    @Override
    public void deleteAllById(List<? extends Long> ids) {
        log.info("Deleting orders by ids: {}", ids);
        Connection conn = transactionManager.getConnection();
        orderDao.deleteAll(conn, ids);
    }

    @Override
    public void deleteAll(List<? extends Order> entities) {
        log.info("Deleting orders: {}", entities);
        Connection conn = transactionManager.getConnection();
        List<Long> ids = entities.stream()
                .map(Order::getId)
                .toList();
        orderDao.deleteAll(conn, ids);
    }

    @Override
    public void deleteAll() {
        log.info("Deleting all orders");
        Connection conn = transactionManager.getConnection();
        orderDao.deleteAll(conn);
    }

    @Override
    public List<Order> findAll(Sort sort) {
        log.info("Getting all orders by sort: {}", sort);
        Connection conn = transactionManager.getConnection();
        return orderDao.getAll(conn, sort);
    }

    @Override
    public Page<Order> findAll(Pageable pageable) {
        log.info("Getting all orders by pageable: {}", pageable);
        Connection conn = transactionManager.getConnection();
        return orderDao.getPage(conn, pageable);
    }
}
