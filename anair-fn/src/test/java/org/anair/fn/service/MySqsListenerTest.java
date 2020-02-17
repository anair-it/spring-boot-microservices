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
import software.amazon.awssdk.services.sqs.model.*;


@ExtendWith(MockitoExtension.class)
class MySqsListenerTest {

    @Mock
    private SqsClient sqsClient;

    @InjectMocks
    private MySqsListener mySqsListener;


    @BeforeEach
    private void setup(){
        mySqsListener.setRequestQ("req");
        mySqsListener.setMaxMessages(10);
        mySqsListener.setVisibilityTimeoutSec(10);
        Assertions.assertEquals("req", mySqsListener.getRequestQ());
    }

    @Test
    void receiveMessage() {
        GetQueueUrlResponse.builder().queueUrl("req").build();
        GetQueueUrlResponse getQueueUrlResponse = GetQueueUrlResponse.builder().queueUrl("resp").build();

        Message message = Message.builder().body("anair").messageId("123").build();

        Mockito.when(sqsClient.getQueueUrl(Mockito.any(GetQueueUrlRequest.class))).thenReturn(getQueueUrlResponse);
        Mockito.when(sqsClient.receiveMessage(Mockito.any(ReceiveMessageRequest.class))).thenReturn(ReceiveMessageResponse.builder().messages(message).build());
        Mockito.when(sqsClient.deleteMessage(Mockito.any(DeleteMessageRequest.class))).thenReturn(Mockito.any(DeleteMessageResponse.class));
        mySqsListener.receiveMessage();

        Mockito.verify(sqsClient, Mockito.times(2)).getQueueUrl(Mockito.any(GetQueueUrlRequest.class));
        Mockito.verify(sqsClient).receiveMessage(Mockito.any(ReceiveMessageRequest.class));
        Mockito.verify(sqsClient).deleteMessage(Mockito.any(DeleteMessageRequest.class));
    }



    @Test
    void receiveMessage_sqsReceive_failed() {
        GetQueueUrlResponse.builder().queueUrl("req").build();
        GetQueueUrlResponse getQueueUrlResponse = GetQueueUrlResponse.builder().queueUrl("resp").build();

        Mockito.when(sqsClient.getQueueUrl(Mockito.any(GetQueueUrlRequest.class))).thenReturn(getQueueUrlResponse);
        Mockito.when(sqsClient.receiveMessage(Mockito.any(ReceiveMessageRequest.class))).thenThrow(new RuntimeException());
        mySqsListener.receiveMessage();

        Mockito.verify(sqsClient).getQueueUrl(Mockito.any(GetQueueUrlRequest.class));
        Mockito.verify(sqsClient).receiveMessage(Mockito.any(ReceiveMessageRequest.class));
        Mockito.verify(sqsClient, Mockito.never()).deleteMessage(Mockito.any(DeleteMessageRequest.class));
    }

}