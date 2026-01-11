package petproject.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import petproject.authservice.dto.CredentialCreateDto;
import petproject.authservice.dto.UserRegisterDto;
import petproject.authservice.model.Credential;
import petproject.authservice.security.jwt.JwtAuthenticationDto;
import petproject.authservice.security.jwt.JwtService;
import petproject.authservice.security.jwt.RefreshTokenDto;
import petproject.authservice.security.jwt.UserCredentialsDto;
import petproject.authservice.service.AuthService;
import petproject.authservice.service.CredentialService;

import javax.naming.AuthenticationException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtService jwtService;
    private final CredentialService credentialService;


    @Override
    public JwtAuthenticationDto signIn(UserCredentialsDto userCredentialsDto) throws AuthenticationException {
        Credential credential = credentialService.findByCredentials(userCredentialsDto);
        return jwtService.generateAuthToken(credential);
    }

    @Override
    public JwtAuthenticationDto refreshToken(RefreshTokenDto refreshTokenDto) throws AuthenticationException {
        String refreshToken = refreshTokenDto.getRefreshToken();
        if (jwtService.validateRefreshToken(refreshToken)) {
            Credential credential = credentialService.findByUserId(jwtService.getUserIdFromRefreshToken(refreshToken));
            return jwtService.refreshBaseToken(credential, refreshToken);
        }
        throw new AuthenticationException("Invalid refresh token");
    }

    @Override
    public JwtAuthenticationDto register(UserRegisterDto userRegisterDto) {
        Credential created = credentialService.createCredential(CredentialCreateDto.builder()
                .email(userRegisterDto.getEmail())
                .password(userRegisterDto.getPassword())
                .build());

        return jwtService.generateAuthToken(created);
    }
}
