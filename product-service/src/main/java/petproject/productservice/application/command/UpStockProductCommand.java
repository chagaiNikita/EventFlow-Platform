package petproject.productservice.application.command;

import java.util.UUID;

public record UpStockProductCommand(
        Integer amount,
        UUID userId,
        UUID productId
) {
}
