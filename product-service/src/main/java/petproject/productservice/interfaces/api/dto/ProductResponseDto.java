package petproject.productservice.interfaces.api.dto;

import petproject.productservice.domain.model.*;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponseDto (
        UUID id,
        UUID sellerId,
        String name,
        String description,
        UUID categoryId,
        BigDecimal price,
        String currency,
        int stock,
        int reserved
)

{}
