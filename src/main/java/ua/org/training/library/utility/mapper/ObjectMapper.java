package ua.org.training.library.utility.mapper;


import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.dto.*;
import ua.org.training.library.model.*;
import ua.org.training.library.security.AuthorityUser;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.PageImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ObjectMapper {
    public HistoryOrder mapOrderToHistoryOrder(Order order) {
        return HistoryOrder.builder()
                .bookTitle(order.getBook().getTitle())
                .dateCreated(order.getDateCreated())
                .dateReturned(LocalDate.now())
                .status(order.getStatus())
                .user(order.getUser())
                .build();
    }

    public AuthorityUser mapUserToAuthorityUser(User user) {
        return new AuthorityUser(
                user.getLogin(),
                user.getFirstName(),
                user.getLastName(),
                user.getRoles()
        );
    }

    public BookDto mapBookToBookDto(Book book) {
        log.info("Map book to book dto");
        log.info("Book: {}", book);
        String authors = book.getAuthors() == null ? "" : book.getAuthors()
                .stream()
                .map(Author::getFullName)
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
        log.info("Authors: {}", authors);
        String keywords = book.getKeywords() == null ? "" : book.getKeywords()
                .stream()
                .map(Keyword::getData)
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
        log.info("Keywords: {}", keywords);
        return BookDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .description(book.getDescription())
                .isbn(book.getIsbn())
                .count(book.getCount())
                .publicationDate(book.getPublicationDate())
                .fine(book.getFine())
                .language(book.getLanguage())
                .location(book.getLocation())
                .authors(authors)
                .keywords(keywords)
                .build();
    }

    public KeywordDto mapKeywordToKeywordDto(Keyword keyword) {
        return KeywordDto.builder()
                .data(keyword.getData())
                .build();
    }

    private AuthorDto mapAuthorToAuthorDto(Author author) {
        return AuthorDto.builder()
                .firstName(author.getFirstName())
                .middleName(author.getMiddleName())
                .lastName(author.getLastName())
                .build();
    }

    public Page<BookDto> mapBookPageToBookDtoPage(Page<Book> page) {
        List<BookDto> bookDtoList = page.getContent()
                .parallelStream()
                .map(this::mapBookPage)
                .toList();
        Pageable pageable = page.getPageable();
        return new PageImpl<>(bookDtoList, pageable, page.getTotalElements());
    }

    private BookDto mapBookPage(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .description(book.getDescription())
                .isbn(book.getIsbn())
                .count(book.getCount())
                .publicationDate(book.getPublicationDate())
                .fine(book.getFine())
                .language(book.getLanguage())
                .location(book.getLocation())
                .build();
    }

    public Page<OrderDto> mapOrderPageToOrderDtoPage(Page<Order> orderPage) {
        List<OrderDto> orderDtoList = orderPage.getContent()
                .parallelStream()
                .map(this::mapOrderToDto)
                .toList();
        Pageable pageable = orderPage.getPageable();
        return new PageImpl<>(orderDtoList, pageable, orderPage.getTotalElements());
    }

    private OrderDto mapOrderToDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .dateCreated(order.getDateCreated())
                .dateExpire(order.getDateExpire())
                .book(mapBookToBookDto(order.getBook()))
                .place(mapPlaceToPlaceDto(order.getPlace()))
                .build();
    }

    private PlaceDto mapPlaceToPlaceDto(Place place) {
        log.info("Map place to place dto");
        return PlaceDto.builder()
                .id(place.getId())
                .name(place.getNames()
                        .get(0)
                        .getName())
                .choosable(place.getChoosable())
                .defaultDate(LocalDate.now().plusDays(place.getDefaultDays()))
                .build();

    }

    public UserDto mapUserToUserDto(User user) {
        return UserDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFullName())
                .login(user.getLogin())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }

    public StatusDto mapStatusToStatusDto(Status status) {
        Map<String, StatusDto> nextStatuses = status.getNextStatuses() == null ?
                Map.of() :
                status.getNextStatuses()
                        .parallelStream()
                        .collect(Collectors.toMap(
                                Status::getCode,
                                this::mapStatusToStatusDto
                        ));
        return StatusDto.builder()
                .code(status.getCode())
                .value(status.getNames().get(0).getName())
                .nextStatuses(nextStatuses)
                .closed(status.getClosed())
                .build();
    }

    public List<PlaceDto> mapPlaceListToPlaceDtoList(List<Place> places) {
        return places.parallelStream()
                .map(this::mapPlaceToPlaceDto)
                .toList();
    }


    public User updateUserData(User user, UserUpdateDto userFromRequest) {
        user.setFirstName(userFromRequest.getFirstName());
        user.setLastName(userFromRequest.getLastName());
        user.setPhone(userFromRequest.getPhone());
        user.setEmail(userFromRequest.getEmail());
        return user;
    }

    public User map(UserRegistrationDto userRegistrationDto) {
        return User.builder()
                .login(userRegistrationDto.getLogin())
                .firstName(userRegistrationDto.getFirstName())
                .lastName(userRegistrationDto.getLastName())
                .email(userRegistrationDto.getEmail())
                .phone(userRegistrationDto.getPhone())
                .build();
    }

    public OrderDto mapOrderToOrderDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .book(mapBookToBookDto(order.getBook()))
                .dateCreated(order.getDateCreated())
                .dateExpire(order.getDateExpire())
                .place(mapPlaceToPlaceDto(order.getPlace()))
                .status(mapStatusToStatusDto(order.getStatus()))
                .user(order.getUser() == null ? null : mapUserToUserDto(order.getUser()))
                .build();
    }

    public OrderDto mapOrderSimpleToOrderDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .book(mapBookToBookDto(order.getBook()))
                .dateCreated(order.getDateCreated())
                .dateExpire(order.getDateExpire())
                .place(mapPlaceToPlaceDto(order.getPlace()))
                .build();
    }

    public Page<OrderDto> mapOrderLibrarianToOrderDto(Page<Order> pageByStatus) {
        List<OrderDto> orderDtoList = pageByStatus.getContent()
                .parallelStream()
                .map(this::mapOrderUserBookToOrderDto)
                .toList();
        Pageable pageable = pageByStatus.getPageable();
        return new PageImpl<>(orderDtoList, pageable, pageByStatus.getTotalElements());
    }

    private OrderDto mapOrderUserBookToOrderDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .book(mapBookToBookDto(order.getBook()))
                .dateCreated(order.getDateCreated())
                .dateExpire(order.getDateExpire())
                .user(mapUserToUserDto(order.getUser()))
                .build();
    }
}
