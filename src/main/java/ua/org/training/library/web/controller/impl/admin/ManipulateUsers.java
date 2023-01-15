package ua.org.training.library.web.controller.impl.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.context.ApplicationContext;
import ua.org.training.library.dto.UserDTO;
import ua.org.training.library.exceptions.ConnectionDBException;
import ua.org.training.library.exceptions.ServiceException;
import ua.org.training.library.model.Role;
import ua.org.training.library.model.User;
import ua.org.training.library.service.RoleService;
import ua.org.training.library.service.UserService;
import ua.org.training.library.utility.Constants;
import ua.org.training.library.utility.DTOMapper;
import ua.org.training.library.utility.Links;
import ua.org.training.library.utility.Utility;
import ua.org.training.library.utility.page.Page;
import ua.org.training.library.utility.page.PageService;
import ua.org.training.library.web.controller.ControllerCommand;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ManipulateUsers implements ControllerCommand {
    private static final Logger LOGGER = LogManager.getLogger(ManipulateUsers.class);
    private final UserService userService = ApplicationContext.getInstance().getUserService();
    private final RoleService roleService = ApplicationContext.getInstance().getRoleService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String[] uriDivided = Utility.getUriParts(request);
        long id;

        if (uriDivided.length == 2) {
            return returnJSONUsers(request, response);
        }
        id = Utility.parseLongOrDefault(uriDivided[2], Constants.APP_DEFAULT_USER_MANIPULATION_ID);
        User user;
        try {
            user = userService.getUserById(id);
            request.setAttribute("account", DTOMapper.userToUserManagementDTO(user));
        } catch (ServiceException e) {
            LOGGER.error("Error while getting user by id", e);
            return Links.USER_INFO_PAGE;
        } catch (ConnectionDBException e) {
            LOGGER.error(e.getMessage(), e);
            return Links.ERROR_PAGE + "?message=" + e.getMessage();
        }
        if (uriDivided.length == 4 && uriDivided[3].equals("edit")) {
            String[] roles = user.getRoles()
                    .stream()
                    .map(Role::getCode)
                    .toArray(String[]::new);
            LOGGER.info(String.format("Role array : %s", Arrays.toString(roles)));
            request.setAttribute("roles", roles);
            try {
                request.setAttribute("rolesList", roleService.getAllRoles());
            } catch (ServiceException e) {
                LOGGER.error("Error while getting roles list", e);
                return Links.USER_INFO_PAGE;
            } catch (ConnectionDBException e) {
                LOGGER.error(e.getMessage(), e);
                return Links.ERROR_PAGE + "?message=" + e.getMessage();
            }
            if (request.getMethod().equals("GET")) {
                return Links.USER_EDIT_PAGE;
            } else {
                LOGGER.debug(request.getParameterValues("role"));
                LOGGER.debug("Try to post my changed users roles!");
                Set<Role> newRoles = new HashSet<>();
                for (String role : request.getParameterValues("role")) {
                    try {
                        newRoles.add(roleService.getRoleByCode(role));
                    } catch (ServiceException e) {
                        LOGGER.error("Error while getting role by code", e);
                        return Links.ADMIN_USER_PAGE_REDIRECT;
                    } catch (ConnectionDBException e) {
                        LOGGER.error(e.getMessage(), e);
                        return Links.ERROR_PAGE + "?message=" + e.getMessage();
                    }
                    LOGGER.info(String.format("New roles : %s", newRoles));
                }
                user.setRoles(newRoles);
                try {
                    userService.updateUserRoles(user);
                } catch (ServiceException e) {
                    LOGGER.error("Error while updating user roles", e);
                    return Links.ADMIN_USER_PAGE_REDIRECT;
                } catch (ConnectionDBException e) {
                    LOGGER.error(e.getMessage(), e);
                    return Links.ERROR_PAGE + "?message=" + e.getMessage();
                }
                return Links.ADMIN_USER_PAGE_REDIRECT;
            }
        }
        return Links.USER_INFO_PAGE;
    }

    private String returnJSONUsers(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        LOGGER.info("Try to get users list in JSON format");
        PageService<User> pageService = new PageService<>();
        Page<User> page = pageService.getPage(request);

        String jsonString = null;
        try {
            jsonString = userService.getUserPage(page);
        } catch (ServiceException e) {
            LOGGER.error("Error while getting users list", e);
        } catch (ConnectionDBException e) {
            LOGGER.error(e.getMessage(), e);
        }
        try {
            PrintWriter writer = response.getWriter();
            writer.print(jsonString);
            LOGGER.info(jsonString);
        } catch (IOException e) {
            LOGGER.error("Error while getting writer from response", e);
        }
        return "";
    }
}
