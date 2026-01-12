package petproject.authservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import petproject.authservice.dto.LogoutDto;
import petproject.authservice.dto.UserRegisterDto;
import petproject.authservice.dto.JwtAuthenticationDto;
import petproject.authservice.dto.RefreshTokenDto;
import petproject.authservice.dto.UserCredentialsDto;
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
    public ResponseEntity<?> refresh(@RequestBody @Valid RefreshTokenDto refreshTokenDto) throws AuthenticationException {
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

    @PostMapping("logout")
    public ResponseEntity<?> logout(@RequestBody @Valid LogoutDto logoutDto) {
        authService.logout(logoutDto.getRefreshToken());
        return ResponseEntity.ok().build();
    }


    @PostMapping("logout-all")
    public ResponseEntity<?> logoutAll(@RequestHeader("Authorization") String authHeader) throws AuthenticationException {
        authService.logoutAll(authHeader);
        return ResponseEntity.ok().build();
    }
}
