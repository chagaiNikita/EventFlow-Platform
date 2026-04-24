package petproject.productservice.interfaces.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import petproject.productservice.application.service.ProductService;
import petproject.productservice.interfaces.kafka.dto.OrderReserveStockEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class StockReservationEventListener {
    private final ProductService productService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "order.reserve.stock")
    public void reserveProduct(ConsumerRecord<String, String> record, Acknowledgment ack) {
        OrderReserveStockEvent event;
        try {
            log.info("Raw message: {}", record.value());
            event = objectMapper.readValue(record.value(), OrderReserveStockEvent.class);

        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize message, skipping. offset={}", record.offset(), e);
            ack.acknowledge();
            return;
        }
        productService.reserveProduct(event.productId(), event.orderId(), event.amount());
        ack.acknowledge();

    }
}
