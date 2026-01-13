package petproject.authservice.service;

import petproject.authservice.model.Credential;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.UUID;

public interface CredentialService {


    Credential createCredential(String email, String password);

    List<Credential> getAllCredentials();

    Credential findByCredentials(String email, String password) throws AuthenticationException;

    Credential findByEmail(String email);

    Credential findByUserId(UUID userId);
}
