package petproject.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import petproject.authservice.exception.RefreshTokenNotFoundException;
import petproject.authservice.model.RefreshToken;
import petproject.authservice.repository.RefreshTokenRepository;
import petproject.authservice.service.RefreshTokenService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;


    @Override
    public RefreshToken save(UUID userId, String refreshToken) {
        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .userId(userId)
                .token(refreshToken)
                .expiresAt(LocalDateTime.now().plusDays(30))
                .createdAt(LocalDateTime.now())
                .build();

        return refreshTokenRepository.save(refreshTokenEntity);
    }

    @Override
    public boolean validateToken(String token) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findRefreshTokenByToken(token);

        if (refreshToken.isEmpty()) {
            return false;
        }

        RefreshToken refreshTokenEntity = refreshToken.get();

        if (refreshTokenEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(refreshTokenEntity);
            return false;
        }
        return true;
    }

    @Override
    public UUID getUserIdFromRefreshToken(String refreshToken) {
        return refreshTokenRepository.findRefreshTokenByToken(refreshToken)
                .map(RefreshToken::getUserId).orElseThrow(RefreshTokenNotFoundException::new);
    }

}
