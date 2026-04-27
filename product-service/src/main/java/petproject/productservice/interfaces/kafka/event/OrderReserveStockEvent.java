package petproject.productservice.interfaces.kafka.event;

import java.util.UUID;

public record OrderReserveStockEvent(
        int amount,
        UUID orderId,
        UUID productId
) {
}
