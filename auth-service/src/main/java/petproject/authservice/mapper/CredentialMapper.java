package petproject.authservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import petproject.authservice.dto.CredentialCreateDto;
import petproject.authservice.model.Credential;

@Mapper(componentModel = "spring")
public interface CredentialMapper {
    @Mapping(source = "password", target = "passwordHash")
    Credential toCredential(CredentialCreateDto credential);
}
