package petproject.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import petproject.authservice.service.CredentialService;

@RestController
@RequestMapping("credentials")
@RequiredArgsConstructor
public class CredentialController {
    private final CredentialService credentialService;

    @GetMapping()
    public ResponseEntity<?> getCredentials() {
        return ResponseEntity.ok(credentialService.getAllCredentials());
    }






}
