package petproject.productservice.infrastructure.persistence.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import petproject.productservice.domain.publisher.StockEventPublisher;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class StockEventPublisherImpl implements StockEventPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void publishReserved(UUID productId, UUID orderId, int amount) {

    }
}
