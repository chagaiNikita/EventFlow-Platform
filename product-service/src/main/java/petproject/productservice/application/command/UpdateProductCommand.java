package petproject.productservice.application.command;

import java.util.UUID;

public record UpdateProductCommand(
        UUID userId,
        UUID productId,
        String name,
        String description,
        UUID categoryId,
        Double price,
        String currency
)
{}
