package petproject.authservice.security.jwt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCredentialsDto {
    private String email;
    private String password;
}
