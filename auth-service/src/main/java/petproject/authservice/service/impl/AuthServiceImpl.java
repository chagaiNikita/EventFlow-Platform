package petproject.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import petproject.authservice.dto.UserRequestDto;
import petproject.authservice.model.Credential;
import petproject.authservice.dto.JwtAuthenticationDto;
import petproject.authservice.security.jwt.JwtService;
import petproject.authservice.dto.RefreshTokenRequestDto;
import petproject.authservice.security.userDetails.CustomUserDetails;
import petproject.authservice.service.AuthService;
import petproject.authservice.service.CredentialService;

import javax.naming.AuthenticationException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtService jwtService;
    private final CredentialService credentialService;


    @Override
    public JwtAuthenticationDto signIn(UserRequestDto userRequestDto) throws AuthenticationException {
        Credential credential = credentialService.findByCredentials(userRequestDto.getEmail(), userRequestDto.getPassword());
        return jwtService.generateAuthToken(credential);
    }

    @Override
    public JwtAuthenticationDto refreshToken(RefreshTokenRequestDto refreshTokenRequestDto) throws AuthenticationException {
        String refreshToken = refreshTokenRequestDto.getRefreshToken();
        if (jwtService.validateRefreshToken(refreshToken)) {
            Credential credential = credentialService.findByUserId(jwtService.getUserIdFromRefreshToken(refreshToken));
            return jwtService.refreshBaseToken(credential, refreshToken);
        }
        throw new AuthenticationException("Invalid refresh token");
    }

    @Override
    public JwtAuthenticationDto register(UserRequestDto userRequestDto) {
        Credential created = credentialService.createCredential(userRequestDto.getEmail(),  userRequestDto.getPassword());
        return jwtService.generateAuthToken(created);
    }

    @Override
    public void logout(String refreshToken) {
        jwtService.deleteToken(refreshToken);
    }


    @Override
    public void logoutAll(Authentication authentication) throws AuthenticationException {
        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        UUID userId = userDetails.credential().getUserId();

        jwtService.deleteAllUserTokens(userId);

    }
}
