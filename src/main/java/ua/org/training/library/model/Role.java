package ua.org.training.library.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.org.training.library.security.GrantedAuthority;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role implements GrantedAuthority {
    private Long id;
    private String name;
    private String code;

    @Override
    public String getAuthority() {
        return code;
    }
}
