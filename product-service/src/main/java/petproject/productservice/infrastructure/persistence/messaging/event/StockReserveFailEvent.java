package petproject.productservice.infrastructure.persistence.messaging.event;

import java.util.UUID;

public record StockReserveFailEvent(
        UUID orderId,
        UUID productId
) {
}
