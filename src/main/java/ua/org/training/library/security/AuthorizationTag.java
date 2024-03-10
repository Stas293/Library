package ua.org.training.library.security;


import jakarta.servlet.jsp.PageContext;
import jakarta.servlet.jsp.tagext.TagSupport;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.org.training.library.enums.DefaultValues;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

@Getter
@Slf4j
@NoArgsConstructor
public class AuthorizationTag extends TagSupport {
    private String role;

    public AuthorizationTag(PageContext pageContext) {
        this.pageContext = pageContext;
    }

    public void setRole(String role) {
        this.role = role
                .replace("(", "")
                .replace(")", "");
    }

    @Override
    public int doStartTag() {
        AuthorityUser authorityUser = Optional.ofNullable(
                (AuthorityUser) pageContext.findAttribute(
                        DefaultValues.USER_ATTRIBUTE.getValue()))
                .orElse(AuthorityUser.ANONYMOUS);
        log.info(role);
        log.info("AuthorityUser: {}", authorityUser);
        try {
            if (getRole().contains("'")) {
                if ((boolean) AuthorityUser.class.getMethod(getRole().split("'")[0], String.class)
                        .invoke(authorityUser, getRole().split("'")[1]))
                    return EVAL_BODY_INCLUDE;
            } else if ((boolean) AuthorityUser.class.getMethod(getRole()).invoke(authorityUser))
                return EVAL_BODY_INCLUDE;
        } catch (NoSuchMethodException e) {
            log.error(String.format("No such method exception: %s", e.getMessage()));
        } catch (IllegalAccessException e) {
            log.error(String.format("Illegal Access Exception: %s", e.getMessage()));
        } catch (InvocationTargetException e) {
            log.error(String.format("Invocation Target Exception: %s", e.getMessage()));
        }
        return SKIP_BODY;
    }
}
