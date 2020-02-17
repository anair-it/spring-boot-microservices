package org.anair.fn.service;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.util.List;

@Service
@Slf4j
@Setter
public class MySqsListener extends SqsService {

    @Value("${sqs.inbound:anair-request-q}")
    private String requestQ;

    @Value("${sqs.inbound.maxMessages:10}")
    private int maxMessages;

    @Value("${sqs.inbound.visibility.timeout.sec:120}")
    private int visibilityTimeoutSec;


    /**
     * Listen to anair-request-q queue. Property: sqs.inbound
     * <p>
     *     Do something with the message(s) and delete them.
     * </p>
     */
    @Scheduled(fixedRateString = "${listener.request.q.rate.ms}")
    public void receiveMessage() {
        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(getQueueUrl(requestQ))
                .maxNumberOfMessages(maxMessages)
                .visibilityTimeout(visibilityTimeoutSec)
                .build();

        List<Message> messages = receiveMessage(receiveMessageRequest);
        processMessage(messages);
    }

    private void processMessage(List<Message> messages) {
        if(CollectionUtils.isNotEmpty(messages)){
            try {
                messages.forEach(message -> log.info("Message: {}", message.body()));
                deleteMessages(messages, requestQ);
            }catch(Exception e){
                log.error("Unable to process messages.", ExceptionUtils.getRootCause(e));
            }
        }
    }

    protected String getRequestQ() {
        return requestQ;
    }
}
