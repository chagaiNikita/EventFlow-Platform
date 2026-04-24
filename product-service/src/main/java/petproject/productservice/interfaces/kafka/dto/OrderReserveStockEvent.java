package petproject.productservice.interfaces.kafka.dto;

import java.util.UUID;

public record OrderReserveStockEvent(
        int amount,
        UUID orderId,
        UUID productId
) {
}
