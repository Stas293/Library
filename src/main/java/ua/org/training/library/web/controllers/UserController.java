package ua.org.training.library.web.controllers;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.*;
import ua.org.training.library.dto.UserDto;
import ua.org.training.library.dto.UserUpdateDto;
import ua.org.training.library.form.PersonalEditFormValidationError;
import ua.org.training.library.security.AuthorityUser;
import ua.org.training.library.security.SecurityService;
import ua.org.training.library.service.UserService;
import ua.org.training.library.utility.mapper.RequestParamsObjectMapper;

@Controller("/user")
@Component
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
    private final SecurityService securityService;
    private final UserService userService;
    private final RequestParamsObjectMapper requestParamsObjectMapper;

    @Get("/personal-data")
    public String getPersonalData(HttpServletRequest request, HttpServletResponse response) {
        log.info("Get personal data");
        AuthorityUser authorityUser = securityService.getAuthorityUser(request);
        UserDto userDto = userService.getByLogin(authorityUser.getLogin()).orElseThrow();
        request.setAttribute("authority", userDto);
        return "/WEB-INF/jsp/user/personal-data.jsp";
    }

    @Get("/personal-data/edit")
    public String getEditPersonalData(HttpServletRequest request, HttpServletResponse response) {
        log.info("Get edit personal data");
        AuthorityUser authorityUser = securityService.getAuthorityUser(request);
        UserDto userDto = userService.getByLogin(authorityUser.getLogin()).orElseThrow();
        request.setAttribute("account", userDto);
        return "/WEB-INF/jsp/user/edit-personal.jsp";
    }

    @Put("/personal-data/edit")
    public String putEditPersonalData(HttpServletRequest request, HttpServletResponse response) {
        log.info("Put edit personal data");
        AuthorityUser authorityUser = securityService.getAuthorityUser(request);
        UserUpdateDto userFromRequest = requestParamsObjectMapper.collectFromEditPersonalForm(request);
        PersonalEditFormValidationError validationError = userService.updatePersonalData(userFromRequest, authorityUser);
        if (validationError.isContainsErrors()) {
            request.setAttribute("errors", validationError);
            request.setAttribute("account", userFromRequest);
            return "/WEB-INF/jsp/user/edit-personal.jsp";
        }
        return "redirect:library/user/personal-data";
    }
}
