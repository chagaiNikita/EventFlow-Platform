package petproject.productservice.application.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import petproject.productservice.domain.model.UserId;

import java.util.UUID;

public record CreateProductCommand (
        UUID userId,
        String name,
        String description,
        UUID categoryId,
        Double price,
        String currency,
        Integer stock
)
{}