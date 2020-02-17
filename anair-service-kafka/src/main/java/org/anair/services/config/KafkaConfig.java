package org.anair.services.config;

import io.opentracing.Tracer;
import io.opentracing.contrib.kafka.spring.TracingConsumerFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

@Configuration
@Slf4j
public class KafkaConfig {

    @Autowired
    private Tracer tracer;

    @Bean
    public ConsumerFactory<String, String> consumerFactory(KafkaConfigProperties kafkaConfigProperties) {
        Map<String, Object> kafkaProps = new HashMap<>();
        kafkaProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigProperties.getBootstrapServers());
        kafkaProps.put(ConsumerConfig.GROUP_ID_CONFIG, "anair");
        kafkaProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        kafkaProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        kafkaProps.put("security.protocol", kafkaConfigProperties.getSsl().get("protocol"));
        return new TracingConsumerFactory<>(new DefaultKafkaConsumerFactory<>(kafkaProps), tracer);
    }

    BiFunction<String, ConsumerRecord, String> consumerSpanNameProvider = (operationName, consumerRecord) -> operationName.toUpperCase();

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(ConsumerFactory<String, String> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(2);
        log.debug("Configured kafka listener factory with 2 consumers");
        return factory;
    }

}
