package ua.org.training.library.security;


import lombok.EqualsAndHashCode;
import ua.org.training.library.constants.Values;
import ua.org.training.library.model.Role;
import ua.org.training.library.model.User;

import java.util.Collection;
import java.util.HashSet;

@EqualsAndHashCode(callSuper = true)
public class AuthorityUser extends User {
    public static final AuthorityUser ANONYMOUS = new AuthorityUser(Values.APP_UNAUTHORIZED_USER);
    
    private AuthorityUser(String login) {
        super(login);
    } 

    public AuthorityUser(String login, String firstName, String lastName, Collection<Role> roles) {
        super(login, firstName, lastName, roles);
    }
    
    public boolean isNonAuthorized() {
        return getGrantedAuthorities().isEmpty();
    }
    
    public boolean isAuthorized() {
        return !getGrantedAuthorities().isEmpty();
    }
    
    public boolean hasRole(String role) {
        return getGrantedAuthorities().stream().anyMatch(r -> r.getAuthority().equals(role));
    }
    
    public Collection<GrantedAuthority> getGrantedAuthorities() {
        return new HashSet<>(getRoles());
    }
}
