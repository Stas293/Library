package ua.org.training.library.web.controllers;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.context.annotations.Autowired;
import ua.org.training.library.context.annotations.Component;
import ua.org.training.library.context.annotations.Controller;
import ua.org.training.library.context.annotations.mapping.Delete;
import ua.org.training.library.context.annotations.mapping.Get;
import ua.org.training.library.context.annotations.mapping.Patch;
import ua.org.training.library.context.annotations.mapping.Put;
import ua.org.training.library.dto.*;
import ua.org.training.library.form.LoggedUserUpdatePasswordFormValidationError;
import ua.org.training.library.form.PersonalEditFormValidationError;
import ua.org.training.library.security.AuthorityUser;
import ua.org.training.library.security.SecurityService;
import ua.org.training.library.service.RoleService;
import ua.org.training.library.service.UserService;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.utility.mapper.JSONMapper;
import ua.org.training.library.utility.mapper.RequestParamsObjectMapper;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.Pageable;

import java.util.Optional;

@Controller
@Component
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
    private final SecurityService securityService;
    private final UserService userService;
    private final RequestParamsObjectMapper requestParamsObjectMapper;
    private final JSONMapper jsonMapper;
    private final RoleService roleService;

    @Get("/user/personal-data")
    public String getPersonalData(HttpServletRequest request, HttpServletResponse response) {
        log.info("Get personal data");
        AuthorityUser authorityUser = securityService.getAuthorityUser(request);
        UserDto userDto = userService.getByLogin(authorityUser.getLogin()).orElseThrow();
        request.setAttribute("authority", userDto);
        return "/WEB-INF/jsp/user/personal-data.jsp";
    }

    @Get("/user/personal-data/edit")
    public String getEditPersonalData(HttpServletRequest request, HttpServletResponse response) {
        log.info("Get edit personal data");
        AuthorityUser authorityUser = securityService.getAuthorityUser(request);
        UserDto userDto = userService.getByLogin(authorityUser.getLogin()).orElseThrow();
        request.setAttribute("account", userDto);
        return "/WEB-INF/jsp/user/edit-personal.jsp";
    }

    @Get("/user/personal-data/password")
    public String getEditPassword(HttpServletRequest request, HttpServletResponse response) {
        log.info("Get edit password");
        return "/WEB-INF/jsp/user/edit-password.jsp";
    }

    @Put("/user/personal-data/edit")
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

    @Put("/user/personal-data/password")
    public String putEditPassword(HttpServletRequest request, HttpServletResponse response) {
        log.info("Put edit password");
        AuthorityUser authorityUser = securityService.getAuthorityUser(request);
        UserLoggedUpdatePasswordDto userFromRequest = requestParamsObjectMapper.collectFromEditPasswordForm(request);
        LoggedUserUpdatePasswordFormValidationError validationError = userService.updatePassword(userFromRequest, authorityUser);
        if (validationError.isContainsErrors()) {
            request.setAttribute("errors", validationError);
            return "/WEB-INF/jsp/user/edit-password.jsp";
        }
        return "redirect:library/user/personal-data";
    }

    @Get("/admin/page")
    public String getAdmin(HttpServletRequest request, HttpServletResponse response) {
        log.info("Get admin");
        return "/WEB-INF/jsp/admin/page.jsp";
    }

    @Get("/admin/manage")
    @SneakyThrows
    public String getAdminManage(HttpServletRequest request, HttpServletResponse response) {
        log.info("Get admin manage");
        Pageable pageable = requestParamsObjectMapper.getPageable(request);
        String search = request.getParameter("search");
        Page<UserDto> userPage = userService.searchUsers(pageable, search);
        jsonMapper.toJson(response.getWriter(), userPage);
        return "";
    }

    @Get("/admin/manage/{id}")
    public String getAdminManageById(HttpServletRequest request, HttpServletResponse response) {
        log.info("Get admin manage by id");
        Long id = Utility.getIdFromUri(request);
        Optional<UserManagementDto> userDto = userService.getUserManagementDtoById(id);
        if (userDto.isPresent()) {
            request.setAttribute("account", userDto.get());
            return "/WEB-INF/jsp/admin/user.jsp";
        }
        return "redirect:library/admin/page";
    }

    @Get("/admin/manage/{id}/edit")
    public String getAdminManageEditById(HttpServletRequest request, HttpServletResponse response) {
        log.info("Get admin manage edit by id");
        Long id = Utility.getIdFromUri(request);
        Optional<UserManagementDto> userDto = userService.getUserManagementDtoById(id);
        if (userDto.isPresent()) {
            request.setAttribute("account", userDto.get());
            request.setAttribute("rolesList", roleService.getAllRoles());
            return "/WEB-INF/jsp/admin/edit-user.jsp";
        }
        return "redirect:library/admin/page";
    }

    @Put("/admin/manage/{id}/edit")
    public String putAdminManageEditById(HttpServletRequest request, HttpServletResponse response) {
        log.info("Put admin manage edit by id");
        UserChangeRolesDto userFromRequest = requestParamsObjectMapper.collectFromEditUserForm(request);
        Optional<UserDto> userDto = userService.updateRolesForUser(userFromRequest);
        return userDto.map(dto -> "redirect:library/admin/manage/" + dto.getId() + "?success=true")
                .orElse("redirect:library/admin/page?success=false");
    }

    @Delete("/admin/manage/{id}")
    public String deleteAdminManageById(HttpServletRequest request, HttpServletResponse response) {
        log.info("Delete admin manage by id");
        Long id = Utility.getIdFromUri(request);
        Optional<UserDto> userDto = userService.deleteUserById(id);
        return userDto.map(dto -> "redirect:library/admin/manage/" + dto.getId() + "?success=true")
                .orElse("redirect:library/admin/page?success=false");
    }

    @Patch("/admin/manage/{id}/disable")
    public String disableAdminManageById(HttpServletRequest request, HttpServletResponse response) {
        log.info("Disable admin manage by id");
        Long id = Utility.getIdFromUri(request);
        Optional<UserDto> userDto = userService.disable(id);
        return userDto.map(dto -> "redirect:library/admin/manage/" + dto.getId() + "?success=true")
                .orElse("redirect:library/admin/page?success=false");
    }

    @Patch("/admin/manage/{id}/enable")
    public String enableAdminManageById(HttpServletRequest request, HttpServletResponse response) {
        log.info("Enable admin manage by id");
        Long id = Utility.getIdFromUri(request);
        Optional<UserDto> userDto = userService.enable(id);
        return userDto.map(dto -> "redirect:library/admin/manage/" + dto.getId() + "?success=true")
                .orElse("redirect:library/admin/page?success=false");
    }
}
