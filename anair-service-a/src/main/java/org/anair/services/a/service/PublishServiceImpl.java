package org.anair.services.a.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PublishServiceImpl implements PublishService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PublishServiceImpl.class);

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Value("${topic.publish}")
    private String publishTopic;

    public void publish(String input){
        kafkaTemplate.send(publishTopic, input);
        LOGGER.info("Published message: {}", input);
    }
}