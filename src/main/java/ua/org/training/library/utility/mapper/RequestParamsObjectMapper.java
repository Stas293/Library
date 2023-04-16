package ua.org.training.library.utility.mapper;

import com.project.university.system_library.context.annotations.Component;
import com.project.university.system_library.dto.*;
import com.project.university.system_library.model.Book;
import com.project.university.system_library.model.Order;
import com.project.university.system_library.model.Place;
import com.project.university.system_library.security.AuthorityUser;
import com.project.university.system_library.utility.page.Pageable;
import com.project.university.system_library.utility.page.impl.PageRequest;
import com.project.university.system_library.utility.page.impl.Sort;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
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

    public Pageable getBookPageable(HttpServletRequest request) {
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
                .id(Long.parseLong(request.getParameter("id")))
                .dateExpire(request.getParameter("dateExpire") == null ? null : LocalDate.parse(request.getParameter("dateExpire")))
                .status(request.getParameter("status"))
                .build();
    }
}
