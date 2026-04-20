package kg.bee.productservice.interfaces.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;


public record CreateProductRequestDto (
        @NotBlank
        String name,
        @NotBlank
        String description,
        @NotBlank
        String category,
        @NotNull
        @Positive
        Double price,
        @NotBlank
        String currency,
        @NotNull
        @Positive
        Integer stock
)
{}
