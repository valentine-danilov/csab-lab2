package by.danilov.cs.lab2.security.details;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude
public class UserDetails {

    private String login;
    private String password;

    public UserDetails() {
    }

    public UserDetails(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
