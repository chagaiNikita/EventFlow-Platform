package petproject.authservice.service;

import petproject.authservice.dto.UserRegisterDto;
import petproject.authservice.dto.JwtAuthenticationDto;
import petproject.authservice.dto.RefreshTokenDto;
import petproject.authservice.dto.UserCredentialsDto;

import javax.naming.AuthenticationException;

public interface AuthService {
    JwtAuthenticationDto signIn(UserCredentialsDto userCredentialsDto) throws AuthenticationException;

    JwtAuthenticationDto refreshToken(RefreshTokenDto refreshTokenDto) throws AuthenticationException;

    JwtAuthenticationDto register(UserRegisterDto userRegisterDto);

    void logout(String refreshToken);

    void logoutAll(String authHeader) throws AuthenticationException;
}
