package petproject.authservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCredentialsDto {
    private String email;
    private String password;
}
