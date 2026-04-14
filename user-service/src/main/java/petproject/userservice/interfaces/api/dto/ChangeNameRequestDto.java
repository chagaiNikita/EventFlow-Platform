package petproject.userservice.interfaces.api.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangeNameRequestDto(
        @NotBlank String firstName,
        @NotBlank String lastName
) {
}
