package ua.org.training.library.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.StringJoiner;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;

    public String getFullName() {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add(firstName);
        if (middleName != null) {
            joiner.add(middleName);
        }
        joiner.add(lastName);
        return joiner.toString();
    }
}
