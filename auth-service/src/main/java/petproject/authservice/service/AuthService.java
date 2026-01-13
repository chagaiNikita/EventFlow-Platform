package petproject.authservice.service;

import org.springframework.security.core.Authentication;
import petproject.authservice.dto.UserRequestDto;
import petproject.authservice.dto.JwtAuthenticationDto;
import petproject.authservice.dto.RefreshTokenRequestDto;

import javax.naming.AuthenticationException;

public interface AuthService {
    JwtAuthenticationDto signIn(UserRequestDto userRequestDto) throws AuthenticationException;

    JwtAuthenticationDto refreshToken(RefreshTokenRequestDto refreshTokenRequestDto) throws AuthenticationException;

    JwtAuthenticationDto register(UserRequestDto userRequestDto);

    void logout(String refreshToken);

    void logoutAll(Authentication authentication) throws AuthenticationException;
}
