package ua.org.training.library.utility;

import ua.org.training.library.dto.*;
import ua.org.training.library.model.*;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DTOMapper {
    private DTOMapper() {
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
                        .map(DTOMapper::authorToDTO)
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
}
