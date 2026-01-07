package petproject.authservice.service;

import petproject.authservice.dto.CredentialCreateDto;
import petproject.authservice.model.Credential;
import petproject.authservice.security.jwt.JwtAuthenticationDto;
import petproject.authservice.security.jwt.RefreshTokenDto;
import petproject.authservice.security.jwt.UserCredentialsDto;

import javax.naming.AuthenticationException;
import java.util.List;

public interface CredentialService {


    Credential createCredential(CredentialCreateDto credentialCreateDto);

    List<Credential> getAllCredentials();

    Credential findByCredentials(UserCredentialsDto userCredentialsDto) throws AuthenticationException;

    Credential findByEmail(String email);
}
