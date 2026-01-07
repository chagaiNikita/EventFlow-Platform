package petproject.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import petproject.authservice.repository.CredentialRepository;
import petproject.authservice.service.CredentialService;
@Service
@RequiredArgsConstructor
public class CredentialServiceImpl implements CredentialService {
    private final CredentialRepository credentialRepository;




}
