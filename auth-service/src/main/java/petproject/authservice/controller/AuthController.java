package petproject.authservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import petproject.authservice.dto.CredentialCreateDto;
import petproject.authservice.dto.UserRegisterDto;
import petproject.authservice.security.jwt.JwtAuthenticationDto;
import petproject.authservice.security.jwt.RefreshTokenDto;
import petproject.authservice.security.jwt.UserCredentialsDto;
import petproject.authservice.service.AuthService;
import petproject.authservice.service.CredentialService;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final CredentialService credentialService;

    @PostMapping
    public ResponseEntity<?> signIn(@RequestBody UserCredentialsDto userCredentialsDto) {
        try {
            JwtAuthenticationDto jwtAuthenticationDto = authService.signIn(userCredentialsDto);
            return ResponseEntity.ok(jwtAuthenticationDto);
        } catch (AuthenticationException e) {
            throw new RuntimeException("Authentication failed" + e.getMessage());
        }
    }

    @PostMapping("refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshTokenDto refreshTokenDto) throws AuthenticationException {
        return ResponseEntity.ok(authService.refreshToken(refreshTokenDto));
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody @Valid UserRegisterDto userRegisterDto) {
        return ResponseEntity.ok(authService.register(userRegisterDto));
    }

    @GetMapping()
    public ResponseEntity<?> getCredentials() {
        return ResponseEntity.ok(credentialService.getAllCredentials());
    }
}
