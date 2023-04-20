package ua.org.training.library.utility.mapper;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.dto.*;
import ua.org.training.library.model.Book;
import ua.org.training.library.model.Order;
import ua.org.training.library.model.Place;
import ua.org.training.library.security.AuthorityUser;
import ua.org.training.library.utility.page.Pageable;
import ua.org.training.library.utility.page.impl.PageRequest;
import ua.org.training.library.utility.page.impl.Sort;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class RequestParamsObjectMapper {
    public UserRegistrationDto collectFromRegistrationForm(HttpServletRequest request) {
        return UserRegistrationDto.builder()
                .login(request.getParameter("login"))
                .firstName(request.getParameter("firstName"))
                .lastName(request.getParameter("lastName"))
                .email(request.getParameter("email"))
                .phone(request.getParameter("phone"))
                .password(request.getParameter("password"))
                .confirmPassword(request.getParameter("confirmPassword"))
                .captcha(request.getParameter("g-recaptcha-response"))
                .build();
    }

    public Order mapRequestToOrder(Book book, Place place, AuthorityUser authorityUser) {
        return Order.builder()
                .book(book)
                .dateCreated(LocalDate.now())
                .user(authorityUser)
                .place(place)
                .build();
    }

    public Pageable getPageable(HttpServletRequest request) {
        int pageNumber = Integer.parseInt(
                request.getParameter("page") == null ? "0" : request.getParameter("page")
        );
        log.info("Page number: " + pageNumber);
        int limit = Integer.parseInt(request.getParameter("limit"));
        log.info("Limit: " + limit);
        String sorting = request.getParameter("sorting");
        log.info("Sorting: " + sorting);
        String sortBy = request.getParameter("sortBy");
        log.info("Sort by: " + sortBy);
        if (sortBy == null || Objects.equals(sortBy, "")) {
            return PageRequest.of(pageNumber, limit);
        }
        return PageRequest.of(pageNumber, limit, Sort.Direction.valueOf(sorting), sortBy);
    }

    public UserUpdateDto collectFromEditPersonalForm(HttpServletRequest request) {
        return UserUpdateDto.builder()
                .firstName(request.getParameter("firstName"))
                .lastName(request.getParameter("lastName"))
                .email(request.getParameter("email"))
                .phone(request.getParameter("phone"))
                .build();
    }

    public UserLoginDto collectFromLoginForm(HttpServletRequest request) {
        return UserLoginDto.builder()
                .login(request.getParameter("login"))
                .password(request.getParameter("password"))
                .build();
    }

    public UserChangePasswordDto collectFromResetPasswordForm(HttpServletRequest request) {
        return UserChangePasswordDto.builder()
                .login(request.getParameter("login"))
                .newPassword(request.getParameter("password"))
                .newPasswordConfirm(request.getParameter("confirmPassword"))
                .build();
    }

    public OrderCreationDto getOrderCreationDto(HttpServletRequest request) {
        return OrderCreationDto.builder()
                .bookId(Long.parseLong(request.getParameter("bookId")))
                .placeId(Long.parseLong(request.getParameter("place")))
                .build();
    }

    public OrderUpdateDto getOrderUpdateDto(HttpServletRequest request) {
        return OrderUpdateDto.builder()
                .id(Long.parseLong(request.getParameter("orderId")))
                .dateExpire(request.getParameter("dateExpire") == null ? null : LocalDate.parse(request.getParameter("dateExpire")))
                .status(request.getParameter("status"))
                .build();
    }

    public UserChangeRolesDto collectFromEditUserForm(HttpServletRequest request) {
        String[] roles = request.getParameterValues("role");
        log.info("Roles: " + Arrays.toString(roles));
        return UserChangeRolesDto.builder()
                .id(Long.parseLong(request.getParameter("id")))
                .roles(Arrays.stream(roles).toList())
                .build();
    }

    public KeywordDto mapKeyword(HttpServletRequest request) {
        return KeywordDto.builder()
                .data(request.getParameter("keyword"))
                .build();
    }

    public BookChangeDto getBookChangeDto(HttpServletRequest request) {
        List<KeywordManagementDto> keywords = Arrays.stream(request.getParameter("keywords")
                .split(","))
                .map(id -> KeywordManagementDto.builder()
                        .id(Long.parseLong(id))
                        .build())
                .toList();
        List<AuthorManagementDto> authors = Arrays.stream(request.getParameter("authors")
                .split(","))
                .map(id -> AuthorManagementDto.builder()
                        .id(Long.parseLong(id))
                        .build())
                .toList();
        return BookChangeDto.builder()
                .title(request.getParameter("title"))
                .count(Integer.parseInt(request.getParameter("count")))
                .isbn(request.getParameter("isbn"))
                .publicationDate(LocalDate.parse(request.getParameter("publicationDate")))
                .fine(Double.parseDouble(request.getParameter("fine")))
                .language(request.getParameter("language"))
                .description(request.getParameter("description"))
                .location(request.getParameter("location"))
                .keywords(keywords)
                .authors(authors)
                .build();
    }

    public AuthorManagementDto getAuthorDto(HttpServletRequest request) {
        return AuthorManagementDto.builder()
                .firstName(request.getParameter("firstName"))
                .middleName(request.getParameter("middleName"))
                .lastName(request.getParameter("lastName"))
                .build();
    }

    public UserLoggedUpdatePasswordDto collectFromEditPasswordForm(HttpServletRequest request) {
        return UserLoggedUpdatePasswordDto.builder()
                .oldPassword(request.getParameter("oldPassword"))
                .password(request.getParameter("password"))
                .confirmPassword(request.getParameter("confirmPassword"))
                .build();
    }
}
