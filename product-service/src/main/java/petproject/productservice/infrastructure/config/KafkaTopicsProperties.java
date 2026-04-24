package petproject.productservice.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("kafka.topics")
public record KafkaTopicsProperties(
        String stockReservedTopic

) {
}
