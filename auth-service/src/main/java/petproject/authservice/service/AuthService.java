package petproject.authservice.service;

import jakarta.validation.Valid;
import petproject.authservice.dto.UserRegisterDto;
import petproject.authservice.security.jwt.JwtAuthenticationDto;
import petproject.authservice.security.jwt.RefreshTokenDto;
import petproject.authservice.security.jwt.UserCredentialsDto;

import javax.naming.AuthenticationException;

public interface AuthService {
    JwtAuthenticationDto signIn(UserCredentialsDto userCredentialsDto) throws AuthenticationException;

    JwtAuthenticationDto refreshToken(RefreshTokenDto refreshTokenDto) throws AuthenticationException;

    JwtAuthenticationDto register(UserRegisterDto userRegisterDto);
}
