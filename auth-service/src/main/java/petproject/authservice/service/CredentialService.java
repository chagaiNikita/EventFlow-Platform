package petproject.authservice.service;

import petproject.authservice.dto.CredentialCreateDto;
import petproject.authservice.model.Credential;
import petproject.authservice.dto.UserCredentialsDto;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.UUID;

public interface CredentialService {


    Credential createCredential(CredentialCreateDto credentialCreateDto);

    List<Credential> getAllCredentials();

    Credential findByCredentials(UserCredentialsDto userCredentialsDto) throws AuthenticationException;

    Credential findByEmail(String email);

    Credential findByUserId(UUID userId);
}
