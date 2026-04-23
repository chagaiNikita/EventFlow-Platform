package petproject.authservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import petproject.authservice.event.UserRegisteredEvent;
import petproject.authservice.model.Outbox;
import petproject.authservice.repository.OutboxRepository;
import petproject.authservice.service.OutboxService;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxServiceImpl implements OutboxService {
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final int BATCH_SIZE = 1000;

    @Override
    public void createOutbox(String eventType, Object payload) {
        Outbox outbox = new Outbox();
        outbox.setId(UUID.randomUUID());
        outbox.setEventType(eventType);
        outbox.setPayload(objectMapper.valueToTree(payload));
        log.info("Outbox payload {}", outbox.getPayload());
        outboxRepository.save(outbox);

    }

    @Scheduled(fixedDelay = 5000)
    public void process() {
        List<Outbox> outboxList = outboxRepository.findPendingMessages(BATCH_SIZE);


        for (Outbox msg : outboxList) {
            kafkaTemplate.send(msg.getEventType(), msg.getPayload())
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            outboxRepository.markAsProcessed(msg.getId());
                        } else {
                            log.error("Failed to send message id={}", msg.getId(), ex);
                        }
                    });
        }
    }



}
