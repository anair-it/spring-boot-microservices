package org.anair.services.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerServiceImpl implements KafkaConsumerService {

    @Override
    @KafkaListener(topics = "anair.publish", groupId = "anair")
    public void consume(String message){
        log.info("Received message: {}", message);
    }
}