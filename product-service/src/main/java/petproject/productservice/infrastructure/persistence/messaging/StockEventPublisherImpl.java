package petproject.productservice.infrastructure.persistence.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import petproject.productservice.domain.publisher.StockEventPublisher;
import petproject.productservice.infrastructure.config.KafkaTopicsProperties;
import petproject.productservice.infrastructure.persistence.messaging.event.StockReserveFailEvent;
import petproject.productservice.infrastructure.persistence.messaging.event.StockReservedEvent;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class StockEventPublisherImpl implements StockEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaTopicsProperties kafkaTopicsProperties;
    private final ObjectMapper objectMapper;

    @Override
    public void publishReserved(UUID productId, UUID orderId, int amount) {
        StockReservedEvent event = new StockReservedEvent(productId, orderId, amount);
        try {
            kafkaTemplate.send(kafkaTopicsProperties.stockReservedTopic(), productId.toString(), objectMapper.writeValueAsString(event))
                    .whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to publish StockReservedEvent orderId={}", orderId, ex);
                }
            });

        } catch (JsonProcessingException e) {
            log.error("Failed to serialize StockReservedEvent for productId={}", productId, e);
            throw new RuntimeException("Event serialization failed", e);
        }
    }

    @SneakyThrows
    @Override
    public void publishReserveFailed(UUID productId, UUID orderId) {
        StockReserveFailEvent stockReserveFailEvent = new StockReserveFailEvent(productId, orderId);
        try {
            kafkaTemplate.send(kafkaTopicsProperties.stockFailedTopic(), productId.toString(),
                    objectMapper.writeValueAsString(stockReserveFailEvent))
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Failed to publish StockReserveFailedEvent orderId={}", orderId, ex);
                        }
                    });
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize StockReservedEvent for productId={}", productId, e);
            throw new RuntimeException("Event serialization failed", e);
        }
    }
}
