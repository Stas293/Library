package ua.org.training.library.security;

import jakarta.servlet.jsp.PageContext;
import jakarta.servlet.jsp.tagext.TagSupport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.org.training.library.utility.Constants;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public class AuthorizationTag extends TagSupport {
    private static final Logger LOGGER = LogManager.getLogger(AuthorizationTag.class);
    private String role;

    public AuthorizationTag() {
    }

    public AuthorizationTag(PageContext pageContext) {
        this.pageContext = pageContext;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role.replace("(", "").replace(")", "");
    }

    @Override
    public int doStartTag() {
        AuthorityUser authorityUser = Optional.ofNullable((AuthorityUser) pageContext.findAttribute(Constants.RequestAttributes.USER_ATTRIBUTE)).orElse(AuthorityUser.ANONYMOUS);
        LOGGER.info(role);
        LOGGER.info(authorityUser);
        try {
            if (getRole().contains("'")) {
                if ((boolean) AuthorityUser.class.getMethod(getRole().split("'")[0], String.class).invoke(authorityUser, getRole().split("'")[1]))
                    return EVAL_BODY_INCLUDE;
            } else if ((boolean) AuthorityUser.class.getMethod(getRole()).invoke(authorityUser))
                return EVAL_BODY_INCLUDE;
        } catch (NoSuchMethodException e) {
            LOGGER.error(String.format("No such method exception: %s", e.getMessage()));
        } catch (IllegalAccessException e) {
            LOGGER.error(String.format("Illegal Access Exception: %s", e.getMessage()));
        } catch (InvocationTargetException e) {
            LOGGER.error(String.format("Invocation Target Exception: %s", e.getMessage()));
        }
        return SKIP_BODY;
    }
}
