package ua.org.training.library.web.controllers;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.constants.Values;
import ua.org.training.library.context.annotations.*;
import ua.org.training.library.dto.UserChangePasswordDto;
import ua.org.training.library.dto.UserLoginDto;
import ua.org.training.library.dto.UserRegistrationDto;
import ua.org.training.library.enums.Links;
import ua.org.training.library.form.RegistrationFormValidation;
import ua.org.training.library.form.ResetValidationError;
import ua.org.training.library.security.AuthorityUser;
import ua.org.training.library.security.SecurityService;
import ua.org.training.library.service.UserService;
import ua.org.training.library.utility.mail.MailService;
import ua.org.training.library.utility.mapper.ObjectMapper;
import ua.org.training.library.utility.mapper.RequestParamsObjectMapper;
import ua.org.training.library.validator.ResetPasswordValidator;
import ua.org.training.library.validator.UserRegistrationValidator;

import java.util.Optional;

@Controller
@Component
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AuthController {
    private final UserService userService;
    private final SecurityService securityService;
    private final UserRegistrationValidator userRegistrationValidator;
    private final RequestParamsObjectMapper requestParamsObjectMapper;
    private final ObjectMapper objectMapper;
    private final MailService mailService;
    private final ResetPasswordValidator resetPasswordValidator;

    @Get("/login")
    public String getLogin(HttpServletRequest request, HttpServletResponse response) {
        log.info("Get login");
        if (securityService.checkIfCurrentUserIsLogged(request)) {
            return Links.USER_PAGE_REDIRECT.getLink();
        }
        return "/WEB-INF/login.jsp";
    }

    @Get("/register")
    public String getRegister(HttpServletRequest request, HttpServletResponse response) {
        log.info("Get register");
        return Links.REGISTRATION_PAGE.getLink();
    }

    @SneakyThrows
    @Post("/login")
    public String postLogin(HttpServletRequest request, HttpServletResponse response) {
        log.info("Post login");

        UserLoginDto userLoginDto = requestParamsObjectMapper.collectFromLoginForm(request);

        Optional<AuthorityUser> authorityUser = userService.logUser(userLoginDto);

        if (authorityUser.isEmpty()) {
            request.setAttribute("error", true);
            return Links.LOGIN_PAGE.getLink();
        }

        securityService.addLoggedUserToContext(request, authorityUser.get());

        return Links.USER_PAGE_REDIRECT.getLink();
    }

    @Post("/register")
    public String postRegister(HttpServletRequest request, HttpServletResponse response) {
        log.info("Post register");
        UserRegistrationDto user = requestParamsObjectMapper.collectFromRegistrationForm(request);

        RegistrationFormValidation registrationFormValidation = userService.save(user);

        if (registrationFormValidation.containsErrors()) {
            request.setAttribute("user_reg", user);
            request.setAttribute("errors", registrationFormValidation);
            return Links.REGISTRATION_PAGE.getLink();
        }
        return Links.LOGIN_PAGE_REDIRECT.getLink();
    }

    @Get("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("Logout");
        AuthorityUser authorityUser = securityService.getAuthorityUser(request);
        securityService.removeLoggedUserFromSession(request, authorityUser.getLogin());
        log.info("User {} logged out", authorityUser.getLogin());
        return Links.MAIN_PAGE_REDIRECT.getLink();
    }

    @Get("/forgot-password")
    public String getForgotPassword(HttpServletRequest request, HttpServletResponse response) {
        log.info("Get forgot password");
        return Links.FORGOT_PASSWORD_PAGE.getLink();
    }

    @Post("/forgot-password")
    public String postForgotPassword(HttpServletRequest request, HttpServletResponse response) {
        log.info("Post forgot password");
        String email = request.getParameter("email");

        if (userService.getByEmail(email).isEmpty()) {
            request.setAttribute("error", true);
            return Links.FORGOT_PASSWORD_PAGE.getLink();
        }

        HttpSession session = request.getSession();

        String token = mailService.sendResetPasswordMail(request.getLocale(), email);
        session.setAttribute("code", token);
        session.setAttribute("email", email);

        return Links.RESET_PASSWORD_PAGE_REDIRECT.getLink();
    }

    @Get("/reset-password")
    public String getResetPassword(HttpServletRequest request, HttpServletResponse response) {
        log.info("Get reset password");
        HttpSession session = request.getSession();
        if (session.getAttribute(Values.CODE) == null
                || session.getAttribute(Values.EMAIL) == null
                || !request.getParameter(Values.CODE).equals(session.getAttribute(Values.CODE))
                || !request.getParameter(Values.EMAIL).equals(session.getAttribute(Values.EMAIL))) {
            return Links.RESET_PASSWORD_PAGE_REDIRECT.getLink();
        }
        request.setAttribute("email", session.getAttribute(Values.EMAIL));
        session.removeAttribute(Values.CODE);
        session.removeAttribute(Values.EMAIL);
        return Links.ENTER_NEW_PASSWORD_PAGE.getLink();
    }

    @Post("/reset-password")
    public String postResetPassword(HttpServletRequest request, HttpServletResponse response) {
        log.info("Post reset password");
        UserChangePasswordDto userChangePasswordDto = requestParamsObjectMapper.collectFromResetPasswordForm(request);

        ResetValidationError resetValidationError = userService.updatePassword(userChangePasswordDto);

        if (resetValidationError.containsErrors()) {
            request.setAttribute("resetValidationError", resetValidationError);
            return Links.ENTER_NEW_PASSWORD_PAGE.getLink();
        }

        return Links.LOGIN_PAGE_REDIRECT.getLink();
    }
}
