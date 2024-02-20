package com.backend.prog.global.config;

import com.backend.prog.domain.feed.dto.KafkaFeedDto;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    @Bean
    public ProducerFactory<String, KafkaFeedDto> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        // Producer가 처음으로 연결할 Kafak Broker의 위치
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9094");
        // Producer가 전송하는 데이터의 Key와 Value의 직렬화 방법
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, KafkaFeedDto> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
