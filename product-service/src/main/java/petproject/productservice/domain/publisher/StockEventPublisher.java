package petproject.productservice.domain.publisher;

import java.util.UUID;

public interface StockEventPublisher {
    void publishReserved(UUID productId, UUID orderId, int amount);

    void publishReserveFailed(UUID productId, UUID orderId);
}
