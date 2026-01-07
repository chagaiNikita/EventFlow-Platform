package petproject.authservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CredentialCreateDto {
    private String email;
    private String password;
}
