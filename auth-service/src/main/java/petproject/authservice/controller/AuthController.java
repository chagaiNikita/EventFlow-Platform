package petproject.authservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import petproject.authservice.dto.UserRequestDto;
import petproject.authservice.dto.JwtAuthenticationDto;
import petproject.authservice.dto.RefreshTokenRequestDto;
import petproject.authservice.service.AuthService;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping
    public ResponseEntity<?> signIn(@RequestBody @Valid UserRequestDto userRequestDto) {
        try {
            JwtAuthenticationDto jwtAuthenticationDto = authService.signIn(userRequestDto);
            return ResponseEntity.ok(jwtAuthenticationDto);
        } catch (AuthenticationException e) {
            throw new RuntimeException("Authentication failed" + e.getMessage());
        }
    }

    @PostMapping("refresh")
    public ResponseEntity<?> refresh(@RequestBody @Valid RefreshTokenRequestDto refreshTokenRequestDto) throws AuthenticationException {
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequestDto));
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody @Valid UserRequestDto userRequestDto) {
        return ResponseEntity.ok(authService.register(userRequestDto));
    }



    @PostMapping("logout")
    public ResponseEntity<?> logout(@RequestBody @Valid RefreshTokenRequestDto refreshTokenRequestDto) {
        authService.logout(refreshTokenRequestDto.getRefreshToken());
        return ResponseEntity.ok().build();
    }


    @PostMapping("logout-all")
    public ResponseEntity<?> logoutAll(@RequestHeader("Authorization") String authHeader) throws AuthenticationException {
        authService.logoutAll(authHeader);
        return ResponseEntity.ok().build();
    }
}
