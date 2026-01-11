package petproject.authservice.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
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

    @Value("123aa5bdf97dfce050e6b4ccdf20ebd5ba9bb74804b2ef89f34f5a4bac6918863dbf0bdce2cfa16f")
    private String jwtSecret;
    private final RefreshTokenService refreshTokenService;

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSingInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
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
            e.printStackTrace();
        }
        return false;
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
}
