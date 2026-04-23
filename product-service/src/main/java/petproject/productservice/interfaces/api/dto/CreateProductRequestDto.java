package petproject.productservice.interfaces.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


public record CreateProductRequestDto (
        @NotBlank
        String name,
        @NotBlank
        String description,
        @NotBlank
        String categoryId,
        @NotNull
        @Positive
        Double price,

        @NotBlank
        @Size(min = 1, max = 3)
        String currency,
        @NotNull
        @Positive
        Integer stock
)
{}
