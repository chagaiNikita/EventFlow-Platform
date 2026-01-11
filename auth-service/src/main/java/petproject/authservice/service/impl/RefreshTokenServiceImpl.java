package petproject.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
    private final PasswordEncoder passwordEncoder;


    @Override
    public RefreshToken save(UUID userId, String refreshToken) {
        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .userId(userId)
                .token(passwordEncoder.encode(refreshToken))
                .expiresAt(LocalDateTime.now().plusDays(30))
                .build();

        return refreshTokenRepository.save(refreshTokenEntity);
    }

    @Override
    public boolean validateToken(String token) {
        String hashedToken = passwordEncoder.encode(token);
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findRefreshTokenByToken(hashedToken);

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
        return refreshTokenRepository.findRefreshTokenByToken(passwordEncoder.encode(refreshToken))
                .map(RefreshToken::getUserId).orElseThrow();
    }

}
