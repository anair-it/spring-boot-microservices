package org.anair.fn.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlResponse;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;


@ExtendWith(MockitoExtension.class)
class MySqsPublisherTest {

    @Mock
    private SqsClient sqsClient;

    @InjectMocks
    private MySqsPublisher mySqsPublisher;


    @BeforeEach
    private void setup(){
        mySqsPublisher.setResponseQ("resp");
        Assertions.assertEquals("resp", mySqsPublisher.getResponseQ());
    }


    @Test
    void sendMessage() {
        GetQueueUrlResponse.builder().queueUrl("resp").build();
        GetQueueUrlResponse getQueueUrlResponse = GetQueueUrlResponse.builder().queueUrl("req").build();

        Mockito.when(sqsClient.getQueueUrl(Mockito.any(GetQueueUrlRequest.class))).thenReturn(getQueueUrlResponse);
        Mockito.when(sqsClient.sendMessage(Mockito.any(SendMessageRequest.class))).thenReturn(SendMessageResponse.builder().messageId("1234").build());
        mySqsPublisher.sendMessage("anair");

        Mockito.verify(sqsClient).getQueueUrl(Mockito.any(GetQueueUrlRequest.class));
        Mockito.verify(sqsClient).sendMessage(Mockito.any(SendMessageRequest.class));
    }

    @Test
    void sendMessage_failed() {
        GetQueueUrlResponse.builder().queueUrl("resp").build();
        GetQueueUrlResponse getQueueUrlResponse = GetQueueUrlResponse.builder().queueUrl("req").build();

        Mockito.when(sqsClient.getQueueUrl(Mockito.any(GetQueueUrlRequest.class))).thenReturn(getQueueUrlResponse);
        Mockito.when(sqsClient.sendMessage(Mockito.any(SendMessageRequest.class))).thenThrow(new RuntimeException());
        mySqsPublisher.sendMessage("anair");

        Mockito.verify(sqsClient).getQueueUrl(Mockito.any(GetQueueUrlRequest.class));
        Mockito.verify(sqsClient).sendMessage(Mockito.any(SendMessageRequest.class));
    }

    @Test
    void sendMessage_null_object() {
        Assertions.assertThrows(NullPointerException.class, () -> mySqsPublisher.sendMessage(null));
    }
}