package petproject.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import petproject.authservice.dto.CredentialCreateDto;
import petproject.authservice.mapper.CredentialMapper;
import petproject.authservice.model.Credential;
import petproject.authservice.repository.CredentialRepository;
import petproject.authservice.security.jwt.UserCredentialsDto;
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
    private final CredentialMapper credentialMapper;

    @Override
    public Credential createCredential(CredentialCreateDto credentialCreateDto) {
        Credential credential = credentialMapper.toCredential(credentialCreateDto);
        credential.setUserId(UUID.randomUUID()); //TODO реализовать подставку реального юзер айди
        credential.setPasswordHash(passwordEncoder.encode(credentialCreateDto.getPassword()));
        credential.setCreatedAt(LocalDateTime.now()); //TODO сделать установку времени по бишкеку
        return credentialRepository.save(credential);
    }

    @Override
    public List<Credential> getAllCredentials() {
        return credentialRepository.findAll();
    }

    @Override
    public Credential findByCredentials(UserCredentialsDto userCredentialsDto) throws AuthenticationException {
        Optional<Credential> optionalCredential = credentialRepository.findByEmail(userCredentialsDto.getEmail());
        if (optionalCredential.isPresent()) {
            Credential credential = optionalCredential.get();
            if (passwordEncoder.matches(userCredentialsDto.getPassword(), credential.getPasswordHash())) {
                return credential;
            }
        }
        throw new AuthenticationException("Email or password is not correct");
    }

    @Override
    public Credential findByEmail(String email) {
        return credentialRepository.findByEmail(email).orElseThrow();
    }








}
