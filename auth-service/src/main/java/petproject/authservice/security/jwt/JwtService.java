package petproject.authservice.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import petproject.authservice.dto.JwtAuthenticationDto;
import petproject.authservice.model.Credential;
import petproject.authservice.service.RefreshTokenService;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtService {

    @Value("${JWT_SECRET}")
    private String jwtSecret;
    private final RefreshTokenService refreshTokenService;

    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSingInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public UUID getUserIdFromRefreshToken(String token) {
        return refreshTokenService.getUserIdFromRefreshToken(token);
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSingInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateRefreshToken(String token) {
        return refreshTokenService.validateToken(token);
    }

    public JwtAuthenticationDto refreshBaseToken(Credential credential, String refreshToken) {
        JwtAuthenticationDto jwtAuthenticationDto = new JwtAuthenticationDto();
        jwtAuthenticationDto.setToken(generateJwtToken(credential));
        jwtAuthenticationDto.setRefreshToken(refreshToken);
        return jwtAuthenticationDto;
    }

    public JwtAuthenticationDto generateAuthToken(Credential credential) {
        JwtAuthenticationDto jwtAuthenticationDto = new JwtAuthenticationDto();
        jwtAuthenticationDto.setToken(generateJwtToken(credential));
        jwtAuthenticationDto.setRefreshToken(generateRefreshToken(credential));
        return jwtAuthenticationDto;
    }

    private String generateJwtToken(Credential credential) {
        Date date = Date.from(LocalDateTime.now().plusMinutes(10).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .subject(credential.getEmail())
                .claim("user_id", credential.getUserId())
                .expiration(date)
                .signWith(getSingInKey())
                .compact();
    }

    private String generateRefreshToken(Credential credential) {
        String token = UUID.randomUUID().toString() + UUID.randomUUID().toString();
        refreshTokenService.save(credential.getUserId(), token);
        return token;
    }

    private SecretKey getSingInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public void deleteToken(String refreshToken) {
        refreshTokenService.deleteToken(refreshToken);
    }

    public void deleteAllUserTokens(String token) {
        Claims claims = getClaimsFromToken(token);
        UUID userId = UUID.fromString(claims.get("user_id").toString());

        refreshTokenService.deleteAllTokensByUserId(userId);
    }
}
