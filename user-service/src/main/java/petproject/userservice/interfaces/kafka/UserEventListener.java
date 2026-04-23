package petproject.userservice.interfaces.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import petproject.userservice.application.service.UserService;
import petproject.userservice.domain.model.UserId;
import petproject.userservice.interfaces.kafka.dto.UserRegisteredEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventListener {
    private final UserService userService;
    private final ObjectMapper objectMapper;


    @KafkaListener(topics = "user.registered")
    public void onUserRegistered(ConsumerRecord<String, String> record, Acknowledgment ack) {
        UserRegisteredEvent event;

        try {
            log.info("Raw message: {}", record.value());
            event = objectMapper.readValue(record.value(), UserRegisteredEvent.class);

        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize message, skipping. offset={}", record.offset(), e);
            ack.acknowledge();
            return;
        }
        userService.createUser(new UserId(event.getUserId()), event.getEmail());
        ack.acknowledge();
    }

}
