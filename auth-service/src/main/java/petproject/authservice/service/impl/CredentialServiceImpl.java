package petproject.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import petproject.authservice.exception.EmailAlreadyExistsException;
import petproject.authservice.model.Credential;
import petproject.authservice.repository.CredentialRepository;
import petproject.authservice.service.CredentialService;

import javax.naming.AuthenticationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CredentialServiceImpl implements CredentialService {
    private final CredentialRepository credentialRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Credential createCredential(String email, String password) {
        if (credentialRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException();
        }

        Credential credential = Credential.builder()
                .email(email)
                .userId(UUID.randomUUID()) //TODO реализовать подставку реального юзер айди
                .passwordHash(passwordEncoder.encode(password))
                .createdAt(LocalDateTime.now()) //TODO сделать установку времени по бишкеку
                .build();
        return credentialRepository.save(credential);
    }

    @Override
    public List<Credential> getAllCredentials() {
        return credentialRepository.findAll();
    }

    @Override
    public Credential findByCredentials(String email, String password) throws AuthenticationException {
        Optional<Credential> optionalCredential = credentialRepository.findByEmail(email);
        if (optionalCredential.isPresent()) {
            Credential credential = optionalCredential.get();
            if (passwordEncoder.matches(password, credential.getPasswordHash())) {
                return credential;
            }
        }
        throw new AuthenticationException("Email or password is not correct");
    }

    @Override
    public Credential findByEmail(String email) {
        return credentialRepository.findByEmail(email).orElseThrow();
    }

    @Override
    public Credential findByUserId(UUID userId) {
        return credentialRepository.findByUserId(userId).orElseThrow();
    }








}
