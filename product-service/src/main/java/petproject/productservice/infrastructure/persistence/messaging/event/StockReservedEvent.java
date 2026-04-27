package petproject.productservice.infrastructure.persistence.messaging.event;

import java.util.UUID;

public record StockReservedEvent(
        UUID orderId,
        UUID productId,
        int amount
) {
}
