package org.anair.fn.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SqsService {

    @Autowired
    private SqsClient sqsClient;

    protected ObjectMapper objectMapper = new ObjectMapper();


    protected void sendMessage(String message, String queueName) {
        Validate.notNull(message);

        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            SendMessageRequest messageRequest = SendMessageRequest.builder()
                    .queueUrl(getQueueUrl(queueName))
                    .messageBody(jsonMessage)
                    .build();

            sendMessage(messageRequest);
        } catch (JsonProcessingException e) {
            log.error("Invalid ItemMessage");
        }

    }

    protected List<Message> receiveMessage(ReceiveMessageRequest receiveMessageRequest) {
        List<Message> messages = new ArrayList<>();
        try {
            messages = sqsClient.receiveMessage(receiveMessageRequest).messages();
            if(CollectionUtils.isNotEmpty(messages)) {
                log.debug("Received {} messages in Q: {}", messages.size(), receiveMessageRequest.queueUrl());
            }else{
                log.trace("Received no messages in Q: {}", receiveMessageRequest.queueUrl());
            }
        }catch(Exception e){
            log.error("Failed to fetch message from Q: {}. Error: {}", receiveMessageRequest.queueUrl(), ExceptionUtils.getRootCauseMessage(e));
        }

        return messages;
    }

    protected void deleteMessages(List<Message> messages, String queue){
        String queueUrl = getQueueUrl(queue);
        messages.forEach(message ->  {
            DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .receiptHandle(message.receiptHandle())
                    .build();
            sqsClient.deleteMessage(deleteMessageRequest);
            log.trace("Deleted message id: {} from Q: {}", message.messageId(), queueUrl);
        });
        log.debug("Deleted {} messages from Q: {}", messages.size(), queueUrl);
    }

    protected String getQueueUrl(String queueName){
        GetQueueUrlRequest getQueueRequest = GetQueueUrlRequest.builder()
                .queueName(queueName)
                .build();
        return sqsClient.getQueueUrl(getQueueRequest).queueUrl();
    }

    private void sendMessage(SendMessageRequest messageRequest) {
        try{
            SendMessageResponse sendMessageResponse = sqsClient.sendMessage(messageRequest);
            if(log.isDebugEnabled()) {
                log.trace("Sent message: {} with message Id: {} to Q: {}", messageRequest.messageBody(), sendMessageResponse.messageId(), messageRequest.queueUrl());
                log.debug("Sent message id: {} to Q: {}", sendMessageResponse.messageId(), messageRequest.queueUrl());
            }
        }catch(Exception e){
            log.error("Failed to send message from Q: {}. Error: {}", messageRequest.queueUrl(), ExceptionUtils.getRootCauseMessage(e));
        }
    }
}
