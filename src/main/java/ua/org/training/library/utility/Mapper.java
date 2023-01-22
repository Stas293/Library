package ua.org.training.library.utility;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.dto.*;
import ua.org.training.library.exceptions.ConnectionDBException;
import ua.org.training.library.exceptions.MapException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.model.*;
import ua.org.training.library.service.AuthorService;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

public class Mapper {
    private static final Logger LOGGER = LogManager.getLogger(Mapper.class);
    private Mapper() {
    }

    public static AuthorDTO authorToDTO(Author author) {
        return AuthorDTO.builder()
                .setId(author.getId())
                .setFirstName(author.getFirstName())
                .setLastName(author.getLastName())
                .createAuthorDTO();
    }

    public static BookDTO bookToDTO(Locale locale, Book book) {
        return BookDTO.builder()
                .setId(book.getId())
                .setName(book.getName())
                .setCount(book.getCount())
                .setISBN(book.getISBN())
                .setPublicationDate(book.getPublicationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString())
                .setFine(Utility.getLocaleFine(locale, book.getFine()))
                .setLanguage(book.getLanguage())
                .setAuthors(book.getAuthors().stream()
                        .map(Mapper::authorToDTO)
                        .toList())
                .createBookDTO();
    }

    public static HistoryOrderDTO historyOrderToDTO(Locale locale, HistoryOrder historyOrder) {
        return HistoryOrderDTO.builder()
                .setId(historyOrder.getId())
                .setUser(userDTO(historyOrder.getUser()))
                .setBookName(historyOrder.getBookName())
                .setDateCreated(historyOrder.getDateCreated()
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                        .toString())
                .setDateExpire(historyOrder.getDateExpire().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString())
                .setStatus(statusToDTO(locale, historyOrder.getStatus()))
                .createHistoryOrderDTO();
    }

    public static OrderDTO orderToDTO(Locale locale, Order order) {
        String dateExpire = order.getDateExpire() != null ?
                order.getDateExpire().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString() :
                Utility.getBundleInterface(locale, Constants.BUNDLE_ORDER_STATUS_PREFIX + "not_set");
        long overdue = order.getDateExpire() != null ? (new Date().toInstant().atOffset(ZoneOffset.UTC).toLocalDate().toEpochDay() -
                order.getDateExpire().toInstant().atOffset(ZoneOffset.UTC).toLocalDate().toEpochDay()) : 0;
        String priceOverdue = Utility.getLocaleFine(locale, order.getBook().getFine() * overdue);
        return OrderDTO.builder()
                .setId(order.getId())
                .setDateCreated(order.getDateCreated().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString())
                .setDateExpire(dateExpire)
                .setBook(bookToDTO(locale, order.getBook()))
                .setUser(userDTO(order.getUser()))
                .setPlace(placeToDTO(locale, order.getPlace()))
                .setStatus(statusToDTO(locale, order.getStatus()))
                .setChooseDateExpire(!order.getStatus().getCode().equals(Constants.ORDER_STATUS_ACCEPT) &&
                        !order.getPlace().getName().equals(Constants.ORDER_PLACE_READING_ROOM))
                .setPriceOverdue(overdue > 0 ? priceOverdue : "0")
                .createOrderDTO();
    }

    public static PlaceDTO placeToDTO(Locale locale, Place place) {
        return PlaceDTO.builder()
                .setName(place.getName())
                .setData(
                        Utility.getBundleInterface(locale,
                                Constants.BUNDLE_PLACE_PREFIX +
                                        place.getName().toLowerCase().replace(" ", ".")))
                .createPlaceDTO();
    }

    public static StatusDTO statusToDTO(Locale locale, Status status) {
        return StatusDTO.builder()
                .setCode(status.getCode())
                .setName(status.getName())
                .setValue(Utility.getBundleInterface(locale, Constants.BUNDLE_ORDER_STATUS_PREFIX
                        + status.getCode().toLowerCase()))
                .setNextStatuses(status
                        .getNextStatuses()
                        .stream()
                        .map(nextStatus -> new Pair<>(
                                nextStatus.getCode(),
                                Utility.getBundleInterface(locale,
                                        Constants.BUNDLE_ORDER_STATUS_PREFIX
                                                + nextStatus.getCode().toLowerCase()))
                        )
                        .toList())
                .createStatusDTO();
    }

    public static UserDTO userDTO(User user) {
        return UserDTO.builder()
                .setLogin(user.getLogin())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setEmail(user.getEmail())
                .setPhone(user.getPhone())
                .createUserDTO();
    }

    public static UserManagementDTO userToUserManagementDTO(User user) {
        return UserManagementDTO.builder()
                .setId(user.getId())
                .setLogin(user.getLogin())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setEmail(user.getEmail())
                .setPhone(user.getPhone())
                .setEnabled(user.isEnabled())
                .setDateCreated(user.getDateCreated())
                .setDateUpdated(user.getDateUpdated())
                .setRoles(user.getRoles() != null?
                        user.getRoles().stream()
                        .map(Role::getCode)
                        .toList() : new ArrayList<>())
                .createUserManagementDTO();
    }

    public static HistoryOrder orderToHistoryOrder(Order order) {
        return HistoryOrder.builder()
                .setUser(order.getUser())
                .setBookName(order.getBook().getName())
                .setDateCreated(order.getDateCreated())
                .setDateExpire(new Date())
                .setStatus(order.getStatus())
                .createHistoryOrder();
    }

    public static Author requestDataToAuthor(HttpServletRequest request) {
        String firstName = Utility.getStringParameter(
                request.getParameter(Constants.RequestAttributes.AUTHOR_FIRST_NAME),
                Constants.APP_STRING_DEFAULT_VALUE);
        String lastName = Utility.getStringParameter(
                request.getParameter(Constants.RequestAttributes.AUTHOR_LAST_NAME),
                Constants.APP_STRING_DEFAULT_VALUE);
        return Author.builder()
                .setFirstName(firstName)
                .setLastName(lastName)
                .createAuthor();
    }

    public static Book requestDataToBook(HttpServletRequest request) {
        String name = Utility.getStringParameter(
                request.getParameter(Constants.RequestAttributes.BOOK_NAME_ATTRIBUTE),
                Constants.APP_STRING_DEFAULT_VALUE);
        int count = Utility.tryParseInt(
                request.getParameter(Constants.RequestAttributes.BOOK_COUNT_ATTRIBUTE),
                Constants.DEFAULT_BOOK_COUNT);
        String ISBN = Utility.getStringParameter(
                request.getParameter(Constants.RequestAttributes.BOOK_ISBN_ATTRIBUTE),
                Constants.APP_STRING_DEFAULT_VALUE);
        Date publicationDate = Utility.parseDateOrDefault(
                request.getParameter(Constants.RequestAttributes.BOOK_PUBLICATION_DATE_ATTRIBUTE),
                Constants.DEFAULT_DATE);
        double fine = Utility.tryParseDouble(
                request.getParameter(Constants.RequestAttributes.BOOK_FINE_ATTRIBUTE),
                Constants.DEFAULT_FINE);
        String language = Utility.getLanguage(
                Locale.of(request.getParameter(Constants.RequestAttributes.LOCALE_ATTRIBUTE)));
        return Book.builder()
                .setName(name)
                .setCount(count)
                .setISBN(ISBN)
                .setPublicationDate(publicationDate)
                .setFine(fine)
                .setLanguage(language)
                .createBook();
    }

    public static List<String> requestDataToAuthorIds(HttpServletRequest request) {
        return Arrays.stream(Utility.getStringParameter(
                                request.getParameter(Constants.RequestAttributes.AUTHOR_IDS_ATTRIBUTE),
                                Constants.APP_STRING_DEFAULT_VALUE)
                        .split(","))
                .toList();
    }

    public static List<Author> authorIdsToAuthors(List<String> authorIds, AuthorService authorService) {
        return authorIds
                .stream()
                .map(Long::parseLong)
                .map((Long id) -> {
                    try {
                        return authorService.getAuthorById(id);
                    } catch (ServiceException e) {
                        LOGGER.error(String.format("Can't get author by id: %d", id), e);
                        throw new MapException(e.getMessage(), e);
                    } catch (ConnectionDBException e) {
                        LOGGER.error(e.getMessage(), e);
                        throw new MapException(e.getMessage(), e);
                    }
                }).toList();
    }
}
