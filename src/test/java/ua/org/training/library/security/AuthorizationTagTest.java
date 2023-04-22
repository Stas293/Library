package ua.org.training.library.security;

import jakarta.servlet.jsp.PageContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.org.training.library.model.Role;
import ua.org.training.library.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class AuthorizationTagTest {
    @Mock
    PageContext pageContext;
    private AuthorizationTag authorizationTag;

    @Test
    void doStartTag() throws NoSuchMethodException {
        Mockito.when(pageContext.findAttribute("user")).thenReturn(null);
        authorizationTag = new AuthorizationTag(pageContext);
        authorizationTag.setRole("hasRole('USER')");
        assertEquals(0, authorizationTag.doStartTag());

        authorizationTag.setRole("isNonAuthorized()");
        assertEquals(1, authorizationTag.doStartTag());

        authorizationTag.setRole("isAuthorized()");
        assertEquals(0, authorizationTag.doStartTag());

        User user = User.builder()
                .id(1L)
                .login("login")
                .phone("phone")
                .email("email")
                .firstName("first")
                .lastName("last")
                .roles(
                        List.of(
                                Role.builder()
                                        .id(1L)
                                        .name("USER")
                                        .code("USER")
                                        .build()
                        )
                )
                .build();
        AuthorityUser authorityUser = new AuthorityUser(
                user.getLogin(),
                user.getFirstName(),
                user.getLastName(),
                user.getRoles()
        );
        Mockito.when(pageContext.findAttribute("user")).thenReturn(authorityUser);
        authorizationTag = new AuthorizationTag(pageContext);
        authorizationTag.setRole("hasRole('USER')");
        assertEquals(1, authorizationTag.doStartTag());

        authorizationTag.setRole("hasRole('ADMIN')");
        assertEquals(0, authorizationTag.doStartTag());
    }
}