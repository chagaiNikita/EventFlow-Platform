package petproject.authservice.security.jwt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtAuthenticationDto {
    private String token;
    private String refreshToken;
}
