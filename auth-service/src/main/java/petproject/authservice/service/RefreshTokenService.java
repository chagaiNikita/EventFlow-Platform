package petproject.authservice.service;

import petproject.authservice.model.RefreshToken;

import java.util.UUID;

public interface RefreshTokenService {
    RefreshToken save(UUID userId, String refreshToken);

    boolean validateToken(String token);

    UUID getUserIdFromRefreshToken(String refreshToken);

    void deleteToken(String refreshToken);

    void deleteAllTokensByUserId(UUID userId);
}
