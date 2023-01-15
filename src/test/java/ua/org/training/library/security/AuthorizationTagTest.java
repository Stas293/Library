package ua.org.training.library.security;

import jakarta.servlet.jsp.PageContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.org.training.library.model.Role;
import ua.org.training.library.model.User;
import ua.org.training.library.utility.Constants;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class AuthorizationTagTest {
    @Mock
    PageContext pageContext;
    private AuthorizationTag authorizationTag;

    @Test
    void doStartTag() throws NoSuchMethodException {

        Mockito.when(pageContext.findAttribute(Constants.RequestAttributes.USER_ATTRIBUTE)).thenReturn(null);
        authorizationTag = new AuthorizationTag(pageContext);
        authorizationTag.setRole("hasRole('USER')");
        assertEquals(0, authorizationTag.doStartTag());

        authorizationTag.setRole("isNonAuthorized()");
        assertEquals(1, authorizationTag.doStartTag());

        authorizationTag.setRole("isAuthorized()");
        assertEquals(0, authorizationTag.doStartTag());

        User user = User.builder()
                .setId(1L)
                .setLogin("login")
                .setPhone("phone")
                .setEmail("email")
                .setFirstName("first")
                .setLastName("last")
                .setRoles(
                        List.of(
                                Role.builder()
                                        .setId(1L)
                                        .setName("USER")
                                        .setCode("USER")
                                        .createRole()
                        )
                )
                .createUser();
        AuthorityUser authorityUser = new AuthorityUser(user);
        Mockito.when(pageContext.findAttribute(Constants.RequestAttributes.USER_ATTRIBUTE)).thenReturn(authorityUser);
        authorizationTag = new AuthorizationTag(pageContext);
        authorizationTag.setRole("hasRole('USER')");
        assertEquals(1, authorizationTag.doStartTag());

        authorizationTag.setRole("hasRole('ADMIN')");
        assertEquals(0, authorizationTag.doStartTag());
    }
}