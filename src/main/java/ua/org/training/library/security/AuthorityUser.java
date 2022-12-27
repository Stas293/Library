package ua.org.training.library.security;

import ua.org.training.library.model.User;
import ua.org.training.library.utility.Constants;

import java.util.Collection;
import java.util.HashSet;

public class AuthorityUser extends User {
    public static final AuthorityUser ANONYMOUS = new AuthorityUser(Constants.APP_UNAUTHORIZED_USER);
    
    private AuthorityUser(String login) {
        super(login);
    } 
    
    public AuthorityUser(User user) {
        this.setLogin(user.getLogin());
        this.setFirstName(user.getFirstName());
        this.setLastName(user.getLastName());
        this.setRoles(user.getRoles());
    }
    
    public boolean isNonAuthorized() {
        return getRoles().isEmpty();
    }
    
    public boolean isAuthorized() {
        return !getRoles().isEmpty();
    }
    
    public boolean hasRole(String role) {
        return getRoles().stream().anyMatch(r -> r.getCode().equals(role));
    }
    
    public Collection<GrantedAuthority> getGrantedAuthorities() {
        return new HashSet<>(getRoles());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthorityUser)) return false;
        if (!super.equals(o)) return false;

        AuthorityUser that = (AuthorityUser) o;

        return getLogin().equals(that.getLogin());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getLogin().hashCode();
        return result;
    }
}
