package kg.bee.productservice.interfaces.api.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequestDto(
        @NotBlank
        String name
)
{}
