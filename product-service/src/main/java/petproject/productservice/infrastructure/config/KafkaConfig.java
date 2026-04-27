package petproject.productservice.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {
    private final KafkaTopicsProperties kafkaTopicsProperties;



    @Bean
    NewTopic createStockReserveTopic() {
        return TopicBuilder
                .name(kafkaTopicsProperties.stockReservedTopic())
                .partitions(3).build();
    }

    @Bean
    NewTopic createStockFailedTopic() {
        return TopicBuilder
                .name(kafkaTopicsProperties.stockFailedTopic())
                .partitions(3).build();
    }



    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
            ConsumerFactory<String, String> consumerFactory,
            KafkaTemplate<String, String> kafkaTemplate
    ) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.setConsumerFactory(consumerFactory);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

        // После всех retry → отправляем в DLQ топик автоматически
        var recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate,
                (record, ex) -> new TopicPartition(record.topic() + ".DLQ", record.partition())
        );

        // 3 попытки с паузой 2 секунды между ними
        var errorHandler = new DefaultErrorHandler(recoverer, new FixedBackOff(2000L, 3L));

        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }
}
