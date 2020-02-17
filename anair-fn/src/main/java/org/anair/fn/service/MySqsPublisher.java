package org.anair.fn.service;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Setter
public class MySqsPublisher extends SqsService {

    @Value("${sqs.outbound:anair-response-q}")
    private String responseQ;

    public void sendMessage(String message) {
        sendMessage(message, responseQ);
    }

    protected String getResponseQ() {
        return responseQ;
    }
}
