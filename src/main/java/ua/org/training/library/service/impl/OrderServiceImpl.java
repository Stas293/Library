package ua.org.training.library.service.impl;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.constants.Values;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.Service;
import ua.org.training.library.context.annotations.Transactional;
import ua.org.training.library.dto.OrderCreationDto;
import ua.org.training.library.dto.OrderDto;
import ua.org.training.library.dto.OrderUpdateDto;
import ua.org.training.library.model.*;
import ua.org.training.library.repository.*;
import ua.org.training.library.security.AuthorityUser;
import ua.org.training.library.service.OrderService;
import ua.org.training.library.utility.mapper.ObjectMapper;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.PageRequest;
import ua.org.training.library.utility.page.impl.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Component
@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final StatusRepository statusRepository;
    private final HistoryOrderRepository historyOrderRepository;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final PlaceRepository placeRepository;

    @Override
    @Transactional
    public Order createModel(Order model) {
        log.info("Creating order: {}", model);
        Status status = statusRepository.findByCode(Values.REGISTERED).orElseThrow();
        User user = userRepository.getByLogin(model.getUser().getLogin()).orElseThrow();
        model.setStatus(status);
        model.setUser(user);
        Book book = model.getBook();
        if (bookRepository.findById(book.getId()).orElseThrow().getCount() == 0) {
            return null;
        }
        Order savedOrder = orderRepository.save(model);
        log.info("Saved order: {}", savedOrder);
        book.setCount(book.getCount() - 1);
        bookRepository.save(book);
        return savedOrder;
    }

    @Override
    @Transactional
    public void updateModel(Order model) {
        log.info("Updating order: {}", model);
        orderRepository.save(model);
    }

    @Override
    @Transactional
    public void deleteModel(Order author) {
        log.info("Deleting order: {}", author);
        orderRepository.delete(author);
    }

    @Override
    @Transactional
    public void createModels(List<Order> models) {
        log.info("Creating orders: {}", models);
        orderRepository.saveAll(models);
    }

    @Override
    @Transactional
    public void updateModels(List<Order> models) {
        log.info("Updating orders: {}", models);
        orderRepository.saveAll(models);
    }

    @Override
    @Transactional
    public void deleteModels(List<Order> models) {
        log.info("Deleting orders: {}", models);
        orderRepository.deleteAll(models);
    }

    @Override
    public List<Order> getAllModels() {
        log.info("Getting all orders");
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getModelsByIds(List<Long> ids) {
        log.info("Getting orders by ids: {}", ids);
        return orderRepository.findAllById(ids);
    }

    @Override
    public long countModels() {
        log.info("Counting orders");
        return orderRepository.count();
    }

    @Override
    public void deleteAllModels() {
        log.info("Deleting all orders");
        orderRepository.deleteAll();
    }

    @Override
    public boolean checkIfExists(Order model) {
        log.info("Checking if order exists: {}", model);
        return orderRepository.existsById(model.getId());
    }

    @Override
    public Page<Order> getModelsByPage(int pageNumber, int pageSize) {
        log.info("Getting orders by page: {}, {}", pageNumber, pageSize);
        return orderRepository.findAll(PageRequest.of(pageNumber, pageSize));
    }

    @Override
    @Transactional
    public void deleteModelById(Long id) {
        log.info("Deleting order by id: {}", id);
        orderRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteModelsByIds(List<Long> ids) {
        log.info("Deleting orders by ids: {}", ids);
        orderRepository.deleteAllById(ids);
    }

    @Override
    public List<Order> getAllModels(String sortField, String sortOrder) {
        log.info("Getting all orders by sort: {}, {}", sortField, sortOrder);
        return orderRepository.findAll(Sort.by(Sort.Direction.fromString(sortOrder), sortField));
    }

    @Override
    public Page<Order> getModelsByPage(int pageNumber, int pageSize, Sort.Direction direction, String... sortField) {
        log.info("Getting orders by page: {}, {}, {}, {}", pageNumber, pageSize, direction, sortField);
        return orderRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortField)));
    }

    @Override
    public Page<Order> getPageByBook(Pageable page, Book book) {
        log.info("Getting orders by book: {}, {}", page, book);
        return orderRepository.getPageByBook(page, book);
    }

    @Override
    public Page<OrderDto> getPageByStatusAndUser(Pageable page, String status, User user, String search, Locale locale) {
        log.info("Getting orders by status and user: {}, {}, {}", page, status, user);
        Status statusOrder = statusRepository.findByCode(status).orElseThrow();
        User userOrder = userRepository.getByLogin(user.getLogin()).orElseThrow();
        if (search != null && !search.isEmpty()) {
            Page<Order> orderPage = orderRepository.getPageByStatusAndUserAndSearch(page, statusOrder, userOrder, search, locale);
            return objectMapper.mapOrderPageToOrderDtoPage(orderPage);
        }
        Page<Order> pageByStatusAndUser = orderRepository.getPageByStatusAndUser(page, statusOrder, userOrder, locale);
        return objectMapper.mapOrderPageToOrderDtoPage(pageByStatusAndUser);
    }

    @Override
    public Page<OrderDto> getPageByStatus(Pageable page, String status, String search) {
        log.info("Getting orders by status: {}, {}", page, status);
        Status statusOrder = statusRepository.findByCode(status).orElseThrow();
        if (search != null && !search.isEmpty()) {
            Page<Order> orderPage = orderRepository.getPageByStatusAndSearch(page, statusOrder, search);
            return objectMapper.mapOrderLibrarianToOrderDto(orderPage);
        }
        Page<Order> pageByStatus = orderRepository.getPageByStatus(page, statusOrder);
        return objectMapper.mapOrderLibrarianToOrderDto(pageByStatus);
    }

    @Override
    @Transactional
    public HistoryOrder save(Order order) {
        log.info("Saving order: {}", order);
        return historyOrderRepository.save(
                objectMapper.mapOrderToHistoryOrder(
                        orderRepository.findById(order.getId()).orElseThrow()
                )
        );
    }

    @Override
    @Transactional
    public Optional<OrderDto> createModel(OrderCreationDto orderCreationDto, AuthorityUser authorityUser) {
        log.info("Creating order: {}, {}", orderCreationDto, authorityUser);
        Book book = bookRepository.findById(orderCreationDto.getBookId()).orElseThrow();
        if (book.getCount() == 0) {
            return Optional.empty();
        }
        Place place = placeRepository.findById(orderCreationDto.getPlaceId()).orElseThrow();
        User user = userRepository.getByLogin(authorityUser.getLogin()).orElseThrow();
        Status status = statusRepository.findByCode(Values.REGISTERED).orElseThrow();
        Order order = Order.builder()
                .user(user)
                .book(book)
                .place(place)
                .status(status)
                .dateCreated(LocalDate.now())
                .build();
        book.setCount(book.getCount() - 1);
        bookRepository.save(book);
        orderRepository.save(order);
        log.info("Saved order: {}", order);
        return Optional.of(objectMapper.mapOrderSimpleToOrderDto(order));
    }

    @Override
    public Optional<OrderDto> getOrderById(long id, Locale locale) {
        log.info("Getting order by id: {}", id);
        return orderRepository.findById(id, locale).map(objectMapper::mapOrderToOrderDto);
    }

    @Override
    public Optional<OrderDto> updateModel(OrderUpdateDto orderUpdateDto) {
        return Optional.empty();
    }

    @Override
    public Optional<OrderDto> getOrderForEdit(long id, Locale locale) {
        log.info("Getting order for edit by id: {}", id);
        Optional<Order> order = orderRepository.findById(id, locale);
        if (order.isPresent()) {
            Status status = order.get().getStatus();
            List<Status> nextStatuses = statusRepository.getNextStatusesForStatusById(status.getId());
            status.setNextStatuses(nextStatuses);
            return Optional.of(objectMapper.mapOrderToOrderDto(order.get()));
        }
        return Optional.empty();
    }
}
