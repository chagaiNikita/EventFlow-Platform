package petproject.userservice.interfaces.api.dto;

import jakarta.validation.constraints.NotBlank;

public record AddAddressRequestDto (
        @NotBlank String address
)
{}
