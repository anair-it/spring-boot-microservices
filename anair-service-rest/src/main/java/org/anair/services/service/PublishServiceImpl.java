package org.anair.services.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PublishServiceImpl implements PublishService {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Value("${topic.publish}")
    private String publishTopic;

    public void publish(String input){
        kafkaTemplate.send(publishTopic, input);
        log.info("Published message: {}", input);
    }
}